const append_dataset_component = new Vue({
    el: "#append_dataset",
    data: {
      datasets: [],
      selectedDataset: null,
      newDataset: null,
      selAttrib: null
    },
    methods: {
      attribChanged(){
        var e = document.getElementById("attribType");
        //var value = e.options[e.selectedIndex].value;
        this.selAttrib = e.options[e.selectedIndex].text;
        
        

        console.log("Attrib Set to: " + this.selAttrib);
      },
      addDataSet(){
        if(this.selectedDataset === null)
          return;

        function Dataset (defn) {
          this.definition = defn;
          this.samples = []; 
        }
                       
        Dataset.prototype.addSample = function(attrib, type, value) {
          this.samples.push({attrib : {type, value}});
        };
        
        if( this.newDataset === null)
          this.newDataset = new Dataset(this.selectedDataset.definition);
          var e = document.getElementById("attribType");
          //var value = e.options[e.selectedIndex].value;
          var text = e.options[e.selectedIndex].text;
          
          var found = this.selectedDataset.definition.attributes.find(function(element) {
            if(element.name === text)
              return element;
            return null;
          });

          if(found !== null){
            this.newDataset.addSample(found.name, found.type, found.values);
          }

          // this.newDataset.addSample("temperature", "floating-point", 75);

        console.log(this.newDataset);
      },
      setSampleName(location, i) {

        
      },      
      setSampleType(location, i) {                

        
      },
      getdataset(location, i) {
        fetch("http://localhost:4567" + location, {
          method: "GET"
        })
        .then(response => response.json())
        .then((data) => {
          this.selectedDataset = data;
        })        
      }
    },
    computed: {
      getSampleCount(){
        if(this.selectedDataset.samples !== undefined || this.selectedDataset.samples != null){
          return this.selectedDataset.samples.length;     
        }
        return 1;
      }
    },
    mounted() {
      fetch("http://localhost:4567/api/datasets")
        .then(response => response.json())
        .then((data) => {
          this.datasets = data;
        })
    },
    template: ` 
    <div>
      <div class="card mb-3">
        <div class="card-header">
          <i class="fa fa-table"></i>Server Provided Datasets</div>
        <div class="card-body">
          <div class="table-responsive">        
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
              <thead>
                <tr>
                  <th>Definition Name</th>
                  <th>Position</th>
                  <th>Select</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="dataset, i in datasets">
                  <td>{{dataset.name}}</td>
                  <td>{{dataset.location}}</td>
                  <td><button v-on:click="getdataset(dataset.location, i)">Choose</button></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div v-if="selectedDataset !== null" class="card mb-3">
        <div align="center">
          <label for="type">Attribute Type</label>
          <select id="attribType" v-on:change="attribChanged()">
            <template v-for="attrib, i in selectedDataset.definition.attributes">  
              <option>{{attrib.type}}</option>
            </template>
          </select>

          <div v-if="selAttrib !== null">
            <div v-if="selAttrib === 'enumerated'">
              <label for="type">Attribute Type</label>
              <select id="attribType" v-on:change="attribChanged()">
                <template v-for="attrib, i in selectedDataset.definition.attributes">  
                  <option>{{attrib.type}}</option>
                </template>
              </select>
            </div>
            <div v-else-if="type === 'floating-point'">
            
              <input id = "name" name = "name" type = "text">
            </div>
            <div v-else-if="type === 'arbitrary'">
            
            </div>
            <div v-else-if="type === 'integer'">
            
            </div>
            <div v-else>
              
            </div>
          </div>

        </div>
        <button v-on:click="addDataSet(selectedDataset)">Add</button>
      </div>

      <div v-if="newDataset !== null" class="card mb-3">
        <div class="card-header">
          <i class="fa fa-table"></i>Sample {{getSampleCount}}</div>
        <div class="card-body">
          <div class="table-responsive">        
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
              <thead>
                <tr>
                  <th>Sample Name</th>
                  <th>Attribute Name</th>
                  <th>Attribute Type</th>
                  <th>Attribute Values</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="obj, o in newDataset.samples">
                  <td>{{o}}</td>
                  <td>{{obj.type}}</td>   
                  <td>{{obj.values}}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>


      <div v-if="selectedDataset !== null" class="card mb-3">
        <div class="card-header">
          <i class="fa fa-table"></i>Dataset: "{{selectedDataset.definition.name}}" Existing Samples </div>
        <div class="card-body">
          <div class="table-responsive">        
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Attribute Name</th>
                  <th>Attribute Type</th>
                  <th>Attribute Values</th>
                </tr>
              </thead>
              <tbody>
                <template v-for="sample, s in selectedDataset.samples">
                  <tr v-for="obj, o in sample">
                    <td>Sample {{s}}</td> 
                    <td>{{o}}</td>
                    <td>{{obj.type}}</td>   
                    <td>{{obj.value}}</td>
                  </tr>
                </template>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    `,
});



{/* <li v-for="dataset, i in datasets">
<div v-if="editdataset === dataset.name">
  <input v-on:keyup.13="updatedataset(dataset)" v-model="dataset.location" />
  <button v-on:click="updatedataset(dataset)">save</button>
</div>
<div v-else>
  <button v-on:click="editdataset = dataset.name">edit</button>
  <button v-on:click="deletedataset(dataset.location, i)">X</button>
  <button v-on:click="getdataset(dataset.location, i)">Get Data</button>
  {{dataset.location}}
</div>
</li> */}