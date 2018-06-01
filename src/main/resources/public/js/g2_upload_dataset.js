const upload_dataset_component = new Vue({
    el: "#upload_dataset",
    data: {
      datasets: []
    },
    methods: {
    attemptUpload() {        
        var input = document.querySelector('input[type="file"]')
        var dataSetName = document.getElementById("name").value; 
        var error = document.getElementById("error");
        
        if( dataSetName !== "" ) {
          function handleErrors(response) {
            if (!response.ok) {
                throw Error(response.statusText);
            }
            return response;
          }

          function log(status){
            console.log(status);
            if (typeof status.statusText != 'undefined')
              error.innerText = status.statusText;
            else
              error.innerText = status;
          }
          
          // Content-types
          // CSV and XLS : "application/vnd.ms-excel"
          // XLSX : application/vnd.openxmlformats-officedocument.spreadsheetml.sheet


          if(input === null || input.files === null || input.files.length === 0){
            log("Select a file.");
            return;
          }

          var file = input.files[0];
          //var type = file.type; // bah lets provide an expected value
          var fname = file.name;
          
          var ext = fname.slice((Math.max(0, fname.lastIndexOf(".")) || Infinity) + 1);
          if(ext === null || ext === undefined){
            log("Unable to determine file type. Select a file with an extension.");
            return;
          }
          var type = "application/" + ext;          

          fetch("http://localhost:4567/api/datasets?name=" + dataSetName, {
            body: file,
            method: "POST",
            headers: {
              "Content-Type": type,
            },
          })
          .then(handleErrors).then(
            success => log(success) // Handle the success response object
          ).catch(
            error => log(error) // Handle the error response object
          );
        } else {
          log("Select a file.");
        }
      }
    },
    // beforeCreate(){

    // },
    // beforeMount(){

    // },
    // beforeDestroyed(){

    // },
    // destroyed(){

    // },
    mounted() {
      fetch("http://localhost:4567/api/datasets")
        .then(response => response.json())
        .then((data) => {
          this.datasets = data;
        })
    },
    template: ` 
      <div>
        <div id="Upload" class="tabcontent">
        <h3>Upload your dataset</h3>
          <form action="">
          Dataset Name:<br>
          <input type="text" name="name" id="name">
          <br>
          </form>
          <br>
            <div>
              <label for="files" class="btn">Select XLS, MAT or CSV:</label>
              <input type="file" id="myFile" accept=".xls,.xlsx,.mat,.csv"/>
            </div>
          <br>
          <button id="upload" v-on:click="attemptUpload()">Upload</button>
          <label for="upload">
            <span class="error" name="error" id="error"></span>
          </label>
          <br>
      </div>
      <br>

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
                </tr>
              </thead>
              <tbody>
                <tr v-for="dataset, i in datasets">
                  <td>{{dataset.name}}</td>
                  <td>{{dataset.location}}</td>                  
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