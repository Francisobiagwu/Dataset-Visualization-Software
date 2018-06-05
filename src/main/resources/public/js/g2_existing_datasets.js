const existing_datasets_component = new Vue({
    el: "#existing_datasets",
    data: {
      editdataset: null,
      datasets: [],
      selectedDataset: null,
    },
    methods: {
      getdataset(location, i) {
        fetch("http://localhost:4567" + location, {
          method: "GET"
        })
        .then(response => response.json())
        .then((data) => {
          this.selectedDataset = data;
        })
      },
      deletedataset(location, i) {
        fetch("http://localhost:4567" + location, {
          method: "DELETE"
        })
        .then(() => {
          this.datasets.splice(i, 1);
        })
      },
      updatedataset(dataset) {
        fetch("http://localhost:4567" + dataset.location, {
          body: JSON.stringify(dataset),
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
        })
        .then(() => {
          this.editdataset = null;
        })
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
                  <td><button v-on:click="getdataset(dataset.location, i)">Get Data</button></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
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

                <tr>
                  <td>Samples</td>
                  <td></td>
                  <td></td>
                  <td></td>
                </tr>
                <template v-for="sample, s in selectedDataset.samples">
                  <tr  v-for="obj, o in sample">
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