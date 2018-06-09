const visualize_dataset_component = new Vue({
    el: "#visualize_dataset",
    data: {
      editdataset: null,
      datasets: [],
      selectedDataset: null,
      visData: null,
    },
    methods: {
      displayData(data){
        this.visData = data;
          
        if(this.visData.style === "series"){
          this.displaySeries();
        } else if(this.visData.style === "histogram"){
          this.displayHistogram();
        } else if(this.visData.style === "scatterplot"){
          this.displayScatter();
        }
      },
      displaySeries(){
        if(!this.visData)
          return;

          var pX = [];
          var pY = [];
  
          if(this.visData.data && this.visData.data !== 0){
            for (var i in this.visData.data) {
              pX.push(this.visData.data[i].value); 
              pY.push(this.visData.data[i].value); 
            }
          }

          var data = [
            {
              x: pX,
              y: pY,
              type: 'scatter'
            }
          ];

          var layout = {
            title: this.visData.name
          };
          
          Plotly.newPlot('chartDiv', data, layout);
      },
      displayHistogram(){
        if(!this.visData)
          return;

        var data = [];

        if(this.visData.data && this.visData.data !== 0){
          for (var i in this.visData.data) {            
            var x1 = [this.visData.data[i].count];            
            data.push( {
                x: x1,
                type: "histogram",
                opacity: 0.5,
                marker: {
                    color: this.visData.data[i].bin.value,
                },
              }
            );
          }
        }
        
        var layout = {
          title: this.visData.name,
          barmode: "overlay", 
        };
        Plotly.newPlot("chartDiv", data, layout);
      },
      displayScatter(){
        if(!this.visData)
          return;

        var data = [];
        var pX = [];
        var pY = [];

        if(this.visData.data && this.visData.data !== 0){
          for (var i in this.visData.data) {
            pX.push(this.visData.data[i].x.value); 
            pY.push(this.visData.data[i].y.value); 
          }
        }

        var trace1 = {
          name: this.visData.name,
          x: pX,
          y: pY,
          mode: 'markers',
          type: 'scatter'
        };

        var layout = { 
          title: this.visData.name
        }; 
        
        var data = [trace1];
        
        Plotly.newPlot('chartDiv', data, layout);
      },
      getvisdata(location, i, type) {
        fetch("/api/visualizations/" + type, {
          method: "GET",
          headers: {
            "Accept": "application/json"
          },
        })
        .then(response => response.json())
        .then((data) => {
          this.displayData(data);
        })
      }
    },
    mounted() {
      fetch("/api/datasets")
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
                  <button v-on:click="getvisdata(dataset.location, i, 'series')">Series</button>
                  <button v-on:click="getvisdata(dataset.location, i, 'scatter')">Scatter</button>
                  <button v-on:click="getvisdata(dataset.location, i, 'histogram')">Histogram</button></td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <div class="card mb-3">
        <div class="card-header"><i class="fa fa-area-chart"></i>Visualization</div>
        <div class="card-body">
          <div id="chartDiv"></div>        
        </div>
      </div>

    </div>
    `,
});
