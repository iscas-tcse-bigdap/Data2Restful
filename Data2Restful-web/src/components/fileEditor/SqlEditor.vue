<template>
    <monaco-editor @onCodeChange="excelTextChanged"  types="sql" :codes="textarea"></monaco-editor>
</template>

<script>
import MonacoEditorVue from './MonacoEditor.vue'

export default {
    components: {
        'monaco-editor': MonacoEditorVue
    },
    data() {
        return {
            textarea:""
        }
    },

    methods: {
        excelTextChanged(text) {
            this.textarea = text;
            this.$emit("changeCode", text);
        },
    }
}

</script>

