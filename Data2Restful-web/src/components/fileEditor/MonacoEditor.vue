<template>
  <div id="monaco-editor-box" >
    <div id="monaco-editor" ref="monacoEditor" />
  </div>
</template>

<script>
import * as monaco from "monaco-editor/esm/vs/editor/editor.main";
export default {
  name:"MonacoEditor",
  components: {},
  props: {
    // 编辑器支持的文本格式,自行在百度上搜索
    types: {
      type: String,
      default:"sql"
    },
    // 名称
    name: {
      type: String,
      default:"test"
    },
    editorOptions: {
      type: Object,
      default: function () {
        return {
          selectOnLineNumbers: true,
          roundedSelection: false,
          readOnly: false, // 只读
          writeOnly: false,
          cursorStyle: "line", //光标样式
          automaticLayout: true, //自动布局
          glyphMargin: true, //字形边缘
          useTabStops: false,
          fontSize: 32, //字体大小
          autoIndent: true, //自动布局
          quickSuggestionsDelay: 100,   //代码提示延时
        };
      },
    },
    codes: {
      type: String,
      default: function () {
        return "";
      },
    },
  },
  data() {
    return {
      editor: null, //文本编辑器
      isSave: true, //文件改动状态，是否保存
      codeValue: null, //保存后的文本
    };
  },
  watch: {
    codes: function (newValue) {
      if (this.editor) {
        if (newValue !== this.editor.getValue()) {
          this.editor.setValue(newValue);
          this.editor.trigger(this.editor.getValue(), 'editor.action.formatDocument')
        }
      }
    }
  },
  mounted() {
    this.initEditor();
  },
  methods: {
    initEditor() {
        this.initTheme();
        const self = this;
        // 初始化编辑器，确保dom已经渲染
        this.editor = monaco.editor.create(self.$refs.monacoEditor, {
            value: self.codeValue || self.codes, // 编辑器初始显示内容
            language: self.types, // 支持语言
            theme: "BlackTheme", // 主题
            selectOnLineNumbers: true, //显示行号
            editorOptions: self.editorOptions,
            scrollbar: {
                useShadows:false,
                vertical:'visible',
                horizontal:'visible',
                horizontalSliderSize: 5,
                verticalSliderSize:16,
                horizontalScrollbarSize:15,
                verticalScrollbarSize:15,
            },
            minimap: { enabled: false },
        });
        // self.$emit("onMounted", self.editor); //编辑器创建完成回调
        self.editor.onDidChangeModelContent(function (event) {
            //编辑器内容changge事件
            self.codesCopy = self.editor.getValue();
            self.$emit("onCodeChange", self.editor.getValue(), event);
        });
    },
    initTheme() {
        /**
         * 自定义主题
         */
        monaco.editor.defineTheme('BlackTheme', {
            base: 'vs-dark',
            inherit: true,
            rules: [{ background: '#08090d' }],  //#202431
            colors: {
                // 相关颜色属性配置
                'editor.background': '#2B2B2B',     //背景色
                'editor.lineHighlightBackground': '#515a6e20',
                'editorLineNumber.foreground': '#008800',
                // 'editor.inactiveSelectionBackground': '#88000015'
            }
        });
        //设置自定义主题
        monaco.editor.setTheme('BlackTheme');
    }
  },
};
</script>

<style scoped>
#monaco-editor {
  width: 100%;
  height: 100%;
  text-align: left;
}
</style>


