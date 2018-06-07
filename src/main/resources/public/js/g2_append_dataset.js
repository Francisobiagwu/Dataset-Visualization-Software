const append_dataset_component = new Vue({
    el: "#append_dataset",
    data: {
      datasets: [],
      location: null,
      selectedDataset: null,
      newSample: null,
      selAttrib: null
    },
    methods: {
      log(status){
        var error = document.getElementById("error");
        console.log(status);
        if (typeof status.statusText != 'undefined')
          error.innerText = status.statusText;
        else
          error.innerText = status;
      },
      createSampleObject(){
        var aValue = null;
        var aLower = null;
        var aUpper = null;
        var aType = this.selAttrib.type;
        var aName = document.getElementById("attribName").value;
        
        if(!aName){
          this.log("Please provide a name for the sample attribute.");
          return null;
        } 

        // Check bounds when applicable so value can be bounded.
        if(aType === "floating-point" || 
          aType === "integer"){
          // Get bounds
          aLower = document.getElementById("attribLower").value;
          aUpper = document.getElementById("attribUpper").value;
          
          if(!aLower){
            this.log("Please specify lower bounds.");
            return null;
          }

          if(!aUpper){
            this.log("Please specify upper bounds.");
            return null;
          }

          if(aLower > aUpper){
            this.log("Lower must be lower than or equal to upper.");
            return null;
          }
        }

        if(aType === "enumerated"){
          var e = document.getElementById("attribSelValue");
          aValue = e.options[e.selectedIndex].text;
        } else {
          aValue = document.getElementById("attribValue").value;
        }

        if(!aValue){
          this.log("Please specify value.");
          return null;
        }
        
        // Check bounded value when applicable.
        if(aType === "floating-point" || 
          aType === "integer"){
          if(aValue < aLower){
            this.log("Value must be greater than or equal to lower.");
            return null;
          }

          if(aValue > aUpper){
            this.log("Value must be less than or equal to upper.");
          }
        }

        return {"name" : aName, "payload" : {"type" : aType, "value" : aValue}};
      },
      attribChanged(){
        var e = document.getElementById("attribType");
        //var value = e.options[e.selectedIndex].value;
        var cboAttrib = e.options[e.selectedIndex].text;
        
        var attribs = this.selectedDataset.definition.attributes;
        var found = null;
        for (var i in attribs) {
          if(attribs[i].type === cboAttrib){
            this.selAttrib = attribs[i];
            break;
          }
        }
        this.log("Attrib Set to: " + this.selAttrib);
      },
      addSampleToSelDefn() {
        if(this.newSample && this.selectedDataset){
          this.selectedDataset.samples.push(this.newSample);
          //Vue.set(this.newSample, {});
          this.newSample = null

          this.postdataset(this.selectedDataset);
        }
      },
      addDataSet(){
        if(this.selectedDataset === null)
          return;

        this.attribChanged();

        if(!this.selAttrib){
          this.log("Please select an attribute type.");
          return;
        }

        var inputObj = this.createSampleObject();

        if(inputObj != null) {
          // We've got valid inputs, add the sample...
          if(!this.newSample){
            this.newSample = {};
          }
          
          Vue.set(this.newSample, inputObj.name, inputObj.payload)
                    
          this.log("Dataset added.");
        }
      },
      setSampleName(location, i) {

        
      },      
      setSampleType(location, i) {                

        
      },
      getdataset(location, i) {
        this.location = location;
        fetch(location, {
          method: "GET"
        })
        .then(response => response.json())
        .then((data) => {
          this.selectedDataset = data;
        })        
      },
      postdataset(dataset) {
        // TODO: REST endpoint does not exist yet...
        
        // location expected to be /api/datasets/:id
        fetch(this.location, {
          body: JSON.stringify(dataset),
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
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
          return this.selectedDataset.samples.length + 1;     
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
          <i class="fa fa-table"></i>Existing Datasets</div>
        <div class="card-body">
          <div class="table-responsive">        
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
              <thead>
                <tr>
                  <th>Definition Name</th>
                  <th>Location</th>
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
        <div>
          <label for="attribName">Property Name</label>
          <input id="attribName" name="attribName" type="text">
        </div>
        <div>
          <label for="attribType">Property Type</label>
          <select id="attribType" v-on:change="attribChanged()">
            <template v-for="attrib, i in selectedDataset.definition.attributes">  
              <option>{{attrib.type}}</option>
            </template>
          </select>
        </div>
        <div v-if="selAttrib !== null"> 
          <input id="attribType" name="attribType" type="hidden" v-model="selAttrib.type">
          <div v-if="selAttrib.type === 'floating-point'">
            <!-- name(string) type(string) bounds(obj : lower : value, upper : value)  value(string)-->
            <label for="attribValue">Value:</label>
            <input id="attribValue" name="attribValue"  type="text">
            <label for="attribUpper">Upper Bounds:</label>
            <input id="attribUpper" name="attribUpper"  type="text">
            <label for="attribLower">Lower Bounds:</label>
            <input id="attribLower" name="attribLower"  type="text"> 
          </div>
          <div v-else-if="selAttrib.type === 'arbitrary'">    
            <!-- name(string) type(string) value(string) -->
            <label for="attribValue">Value:</label>
            <input id="attribValue" name="attribValue"  type="text">  
          </div>
          <div v-else-if="selAttrib.type === 'integer'">
            <!-- name(string) type(string) bounds(obj : lower : value, upper : value) -->
            <label for="attribValue">Value:</label>
            <input id="attribValue" name="attribValue"  type="text">
            <label for="attribUpper">Upper Bounds:</label>
            <input id="attribUpper" name="attribUpper"  type="text">
            <label for="attribLower">Lower Bounds:</label>
            <input id="attribLower" name="attribLower"  type="text">  
          </div>
          <div v-else>
            <!-- name(string) type(string) values(array) value(selected option) -->
            <div>
              <label for="attribSelValue">Property Type</label>
              <select id="attribSelValue">
                <template v-for="value, i in selAttrib.values">  
                  <option>{{value}}</option>
                </template>
              </select>
            </div>
          </div>
        </div>
        <button v-model="newSample" id="add" v-on:click="addDataSet()">Add</button>          
        <label for="add">
          <span class="error" name="error" id="error"></span>
        </label>
      </div>
      </div>

      <div v-if="newSample !== null" class="card mb-3">
        <div class="card-header">
          <i class="fa fa-table"></i>Sample {{getSampleCount}}
        </div>
        <div class="card-body">
          <div class="table-responsive">        
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Type</th>
                  <th>Values</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="obj, o in newSample">
                  <td>{{o}}</td>
                  <td>{{obj.type}}</td>   
                  <td>{{obj.value}}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <button v-model="selectedDataset" id="append" name="append" v-on:click="addSampleToSelDefn()">Add</button>          
        <label for="append">
          <span class="error" name="appEndError" id="appEndError"></span>
        </label>
      </div>

      <div v-if="selectedDataset !== null" class="card mb-3">
        <div class="card-header">
          <i class="fa fa-table"></i>Dataset: "{{selectedDataset.definition.name}}" Existing Samples </div>
        <div class="card-body">
          <div class="table-responsive">        
            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
              <thead>
                <tr>
                  <th>Sample</th>
                  <th>Name</th>
                  <th>Type</th>
                  <th>Values</th>
                </tr>
              </thead>
              <tbody>
                <template v-for="sample, s in selectedDataset.samples">
                  <tr v-for="obj, o in sample">
                    <td>{{s + 1}}</td> 
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
