<template>
    <div></div>
</template>

<script>
import { mapMutations } from 'vuex';

export default {
    created() {    
        this.SET_HEADER_TITLE("PROJECT MANAGEMENT");
    },
    methods: {
        ...mapMutations(["SET_HEADER_TITLE"]),
    }
}
</script>

<style>
.v-treeview-node__root {
  min-height: 35px;
}
</style>