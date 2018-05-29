const app = new Vue({
    el: "#app",
    data: {
      editFriend: null,
      friends: [],
    },
    methods: {
      deleteFriend(id, i) {
        fetch("http://rest.learncode.academy/api/vue-5/friends/" + id, {
          method: "DELETE"
        })
        .then(() => {
          this.friends.splice(i, 1);
        })
      },
      updateFriend(friend) {
        fetch("http://rest.learncode.academy/api/vue-5/friends/" + friend.id, {
          body: JSON.stringify(friend),
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
        })
        .then(() => {
          this.editFriend = null;
        })
      }
    },
    mounted() {
      fetch("http://localhost:4567/api/datasets")
        .then(response => response.json())
        .then((data) => {
          this.friends = data;
        })
    },
    template: ` 
    <div>
      <li v-for="friend, i in friends">
        <div v-if="editFriend === friend.name">
          <input v-on:keyup.13="updateFriend(friend)" v-model="friend.location" />
          <button v-on:click="updateFriend(friend)">save</button>
        </div>
        <div v-else>
          <button v-on:click="editFriend = friend.name">edit</button>
          <button v-on:click="deleteFriend(friend.name, i)">x</button>
          {{friend.location}}
        </div>
      </li>
    </div>
    `,
});