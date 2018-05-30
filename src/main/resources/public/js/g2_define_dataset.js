const define_dataset_component = new Vue({
  el: "#define_dataset",
  data: {
    datasets: [],
    location: null,
    selectedDataset: null,
    newAttributes: null,
    selAttrib: null,
    attrib_types: [ "enumerated", "floating-point", "arbitrary", "integer"]
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
      var aLower = null;
      var aUpper = null;
      var aType = this.selAttrib;
      var aName = document.getElementById("attribName").value;
      
      if(!aName){
        this.log("Please provide a name for the sample attribute.");
        return null;
      }

      if(this.newAttributes && this.selectedDataset){
        var text = "";
        this.newAttributes.forEach(function(t) {

          // this.selectedDataset.definition.attributes.forEach(function(t) {
          //   if(t.name === aName){
          //     this.log("Please provide a unique name." + "'" + aName + "'" +
          //      "is within original dataset defintion.");
          //     return null;
          //   }
          // });

          if(t.name === aName){
            text = "Please provide a unique name.";
            return null;
          }
        });

        if(text){
          this.log(text);
          return null;
        }
      }

      // No validation nessesary for arbitrary 

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
        var aValue = document.getElementById("attribValue").value;
        if(!aValue){
          this.log("Please specify value.");
          return null;
        }

        // split by comma, remove dups.
        var tokens= Array.from(new Set(aValue.split(',')));

        var reformed = [];
        tokens.forEach(function(t) {
          t = t.trim();
          t = t.replace(" ", "_");
          reformed.push(t);  
        });
        aValue = reformed;
      } 
      
      // Format definiton.attributes entry given type
      if(aType === "enumerated"){
        return {"name" : aName, "type" : aType, "values" : aValue};
      }else if(aType === "floating-point" || aType === "integer"){
        return {"name" : aName, "type" : aType, "bounds" : {"upper" : aUpper, "lower": aLower} };
      }else if(aType === "arbitrary"){
        return {"name" : aName, "type" : aType };
      }

      return null;        
    },
    attribChanged(){
      var e = document.getElementById("attribType");
      //var value = e.options[e.selectedIndex].value;
      var cboAttrib = e.options[e.selectedIndex].text;
      
      var attribs = this.attrib_types;
      var found = null;
      for (var i in attribs) {
        if(attribs[i] === cboAttrib){
          this.selAttrib = attribs[i];
          break;
        }
      }
      console.log("Attrib Set to " + this.selAttrib);
    },
    addSampleToSelDefn() {
      if(this.newAttributes && this.selectedDataset){
        for (var i in this.newAttributes) {
          this.selectedDataset.definition.attributes.push(this.newAttributes[i]);
        }        
        this.newAttributes = null

        //this.postdataset(this.selectedDataset);
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
        
        if(!this.newAttributes){
          this.newAttributes = [];
        }
        
        this.newAttributes.push(inputObj);
      }
    },
    setSampleName(location, i) {

      
    },      
    setSampleType(location, i) {                

      
    },
    getdataset(location, i) {
      this.location = location;
      fetch("http://localhost:4567" + location, {
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
      fetch("http://localhost:4567" + this.location, {
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
        <label for="attribName">New Attribute Name</label>
        <input id="attribName" name="attribName" type="text">
      </div>
      <div>
        <label for="attribType">New Attribute Type</label>
        <select id="attribType" v-on:change="attribChanged()">
          <template v-for="attrib, i in attrib_types">
            <option>{{attrib}}</option>
          </template>
        </select>
      </div>
      <div v-if="selAttrib !== null"> 
        <input id="attribType" name="attribType" type="hidden" v-model="selAttrib">
        <div v-if="selAttrib === 'floating-point'">
          <!-- name(string) type(string) bounds(obj : lower : value, upper : value)  value(string)-->
          <label for="attribUpper">Upper Bounds:</label>
          <input id="attribUpper" name="attribUpper"  type="text">
          <label for="attribLower">Lower Bounds:</label>
          <input id="attribLower" name="attribLower"  type="text"> 
        </div>
        <div v-else-if="selAttrib === 'arbitrary'">    
          <!-- name(string) type(string) -->   
          <!-- no controls nessesary -->
        </div>
        <div v-else-if="selAttrib === 'integer'">
          <!-- name(string) type(string) bounds(obj : lower : value, upper : value) -->
          <label for="attribUpper">Upper Bounds:</label>
          <input id="attribUpper" name="attribUpper"  type="text">
          <label for="attribLower">Lower Bounds:</label>
          <input id="attribLower" name="attribLower"  type="text">  
        </div>
        <div v-else>
          <!-- name(string) type(string) values(array) value(selected option) -->
          <div>
            <label for="attribValue">Accepted Values(comma seperated):</label>
            <input id="attribValue" name="attribValue"  type="text">
          </div>
        </div>
      </div>
      <button v-model="newAttributes" id="add" v-on:click="addDataSet()">Add</button>          
      <label for="add">
        <span class="error" name="error" id="error"></span>
      </label>
    </div>
    </div>

    <div v-if="newAttributes !== null" class="card mb-3">
      <div class="card-header">
        <i class="fa fa-table"></i>Sample {{getSampleCount}}
      </div>
      <div class="card-body">
        <div class="table-responsive">        
          <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
            <thead>
              <tr>
                <th>Attribute Name</th>
                <th>Attribute Type</th>
                <th>Optional Details</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="obj, o in newAttributes">
                <td>{{obj.name}}</td>
                <td>{{obj.type}}</td>
                <td v-if="obj.type == 'enumerated'">
                  {{obj.values}}
                </td>
                <td v-else-if="obj.type == 'floating-point' || obj.type == 'integer'">
                   upper: {{obj.bounds.upper}}, lower: {{obj.bounds.lower}}
                </td>
                <td v-else></td>
              </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <button id="append" name="append" v-on:click="addSampleToSelDefn()">Add</button>          
      <label for="append">
        <span class="error" name="appEndError" id="appEndError"></span>
      </label>
    </div>
    
    <div v-if="selectedDataset !== null" class="card mb-3">
      <div class="card-header">
        <i class="fa fa-table"></i>Selected Dataset Data</div>
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
              <tr>
                <td>{{selectedDataset.definition.name}}</td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              <tr v-for="attrib, i in selectedDataset.definition.attributes">  
                <td></td>              
                <td>{{attrib.name}}</td>
                <td>{{attrib.type}}</td>
                <td>{{attrib.values}}</td>
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