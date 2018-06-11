const visualize_Defn_DataDefn_component = new Vue({
  el: "#visualize_Defn_DataDefn",
  data: {
    datasets: null,
    visDatasets: null,
    location: null,
    selectedDataset: null,
    selChartType: "",
    visName: "",
    selAttribTwo: null,
    selAttribOne: null,
    chart_types: [ "histogram", "scatter", "series"]
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
            
      if(!this.visName){
        this.log("Please provide a dataset name for the sample attribute.");
        return null;
      }

      var bailBadDatasetName = false;
      if(this.visDatasets){
        this.visDatasets.forEach(function(t) {
          if(t.name === this.visName){
            bailBadDatasetName = true;
          }
        }); 
      }

      if(bailBadDatasetName){        
        this.log("Please provide a unique visualization name. This one already exists.");
        return;
      }
      
      if(!this.selAttribOne){
        this.log("Please choose the first attribute.");
        return;
      }

      if(this.selChartType == "scatter" && !this.selAttribTwo){      
        this.log("Please choose the second attribute.");
        return;
      }

      var attribObj1 = null;
      var attribObj2 = null;
      var found = null;
      for (var i in this.selectedDataset.definition.attributes) {
        if(this.selectedDataset.definition.attributes[i].name === this.selAttribOne){
          attribObj1 = this.selectedDataset.definition.attributes[i];
        }

        if(this.selectedDataset.definition.attributes[i].name === this.selAttribTwo){
          attribObj2 = this.selectedDataset.definition.attributes[i];
        }
      }

      if(this.selChartType == "scatter"){
        this.postdataset({ "name" : this.visName, "location": this.location,  "attributes" : [attribObj1, attribObj2] });
      } else {
        this.postdataset({ "name" : this.visName, "location": this.location,  "attributes" : [attribObj1] });
      } 
      
      
      return null;        
    },
    addVisualization(){
      var inputObj = this.createSampleObject();

      if(inputObj != null) {
        // We've got valid inputs, add the sample...
        
        if(!this.newAttributes){
          this.newAttributes = [];
        }
        
        this.newAttributes.push(inputObj);
      }
    },
    getData(){
      fetch("/api/datasets")
      .then(response => response.json())
      .then((data) => {
        if(data && data.length > 0){
          this.datasets = data;
        } else {
          this.datasets = null;
        }
      })     
    },
    getVisData(){
      fetch("/api/visualizations", {
        method: "GET",
        headers: {
          "Accept": "application/json"
        },
      })
      .then(response => response.json())
      .then((data) => {
        this.visDatasets = data;
      })
    },
    getDataset(location, i) {
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
      function handleErrors(response) {
        if (!response.ok) {
            throw Error(response.statusText);
        }
        return response;
      }

      fetch("/api/visualizations", {
        body: JSON.stringify(dataset.definition),
        method: "POST",
        headers: {
          "Accept": "application/json"
        },
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then(handleErrors).then(
        (success) => {
          this.log(success);
          this.getData();  
        }
      ).catch(
        error => console.log(error) // Handle the error response object
      );

      this.selectedDataset = null;
      this.selAttrib = null;
      this.selChartType = "",
      this.visName = "",
      this.selAttrib2 = null,
      this.selAttrib1 = null,

      this.getData();
    }
  },
  mounted() {
    this.getData();
    this.getVisData();
  },
  template: ` 
  <div>

  <div v-if="datasets !== null" class="card mb-3">
    <div class="card mb-3">
    <div class="card-header">
      <i class="fa fa-table"></i>Choose existing dataset to visualize:</div>
    <div class="card-body">
      <div class="table-responsive">        
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
          <thead>
            <tr>
            <th>Available Datasets</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="dataset, i in datasets">
            <button class="btn btn-link" v-on:click="getDataset(dataset.location, i)">{{dataset.name}}</button>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  
  <div v-if="selectedDataset !== null" class="card mb-3">
    <div align="center">
      <div v-if="selectedDataset !== null">
        <label for="datasetName">Visualization Name</label>
        <input v-model="visName" id="datasetName" name="datasetName" type="text">
      </div>
      <div>
        <label for="cTypeType">Visualization Type:</label>
        <select id="cTypeType" v-model="selChartType">
          <template v-for="cType, i in chart_types">
            <option>{{cType}}</option>
          </template>
        </select>
      </div>
      <div v-if="selChartType !== null"> 
        <input id="attribType" name="attribType" type="hidden" v-model="selChartType">
        <div v-if="selChartType === 'scatter'">
          <!-- two attributes from the dataset which are of the following types: {floating-point, integer} -->
          <label for="attribOne">Choose Attribute:</label>
          <select id="attribOne" v-model="selAttribOne">
            <template v-for="defn, i in selectedDataset.definition.attributes">
              <option>{{defn.name}}</option>
            </template>
          </select>
          <label for="attribTwo">Choose Attribute:</label>
          <select id="attribTwo" v-model="selAttribTwo">
            <template v-for="defn, i in selectedDataset.definition.attributes">
              <option>{{defn.name}}</option>
            </template>
          </select>
        </div>
        <div v-else-if="selChartType === 'histogram'">    
          <!-- one attribute from the dataset which is one of the following types: {integer, enumerated, arbitrary} -->
          <label for="attribOne">Choose Attribute:</label>
          <select id="attribOne" v-model="selAttribOne">  
            <template v-for="defn, i in selectedDataset.definition.attributes">
              <option>{{defn.name}}</option>
            </template>
          </select>
        </div>
        <div v-else-if="selChartType === 'series'">
          <!-- one attribute from the dataset which is of one of the following types: {integer, floating-point} -->
          <label for="attribOne">Choose Attribute:</label>
          <select id="attribOne" v-model="selAttribOne">
            <template v-for="defn, i in selectedDataset.definition.attributes">
              <option>{{defn.name}}</option>
            </template>
          </select>
        </div>
      </div>      
      <button id="add" v-on:click="addVisualization()">Submit Visualization Dataset</button>          
      <label for="add">
        <span class="error" name="error" id="error"></span>
      </label>
    </div>
    </div>

    <div class="card mb-3">
    <div class="card-header">
      <i class="fa fa-table"></i>Server Provided Visualization Datasets</div>
    <div class="card-body">
      <div class="table-responsive">        
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
          <thead>
            <tr>
              <th>Available Visualizations</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="dataset, i in visDatasets">
              <p>{{dataset.name}}</p>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
    
    <div v-if="selectedDataset !== null" class="card mb-3">
      <div class="card-header">
        <i class="fa fa-table"></i>Dataset Definition "{{selectedDataset.definition.name}}"</div>
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

  </div>
  `,
});
