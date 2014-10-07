define("ace/theme/vwml_crimson_editor",["require","exports","module","ace/lib/dom"], function(require, exports, module) {
exports.isDark = false;
exports.cssText = ".ace-vwml-crimson-editor .ace_gutter {\
background: #ebebeb;\
color: #333;\
overflow : hidden;\
}\
.ace-vwml-crimson-editor .ace_gutter-layer {\
width: 100%;\
text-align: right;\
}\
.ace-vwml-crimson-editor .ace_print-margin {\
width: 1px;\
background: #e8e8e8;\
}\
.ace-vwml-crimson-editor {\
background-color: #FFFFFF;\
color: rgb(64, 64, 64);\
}\
.ace-vwml-crimson-editor .ace_cursor {\
color: black;\
}\
.ace-vwml-crimson-editor .ace_invisible {\
color: rgb(191, 191, 191);\
}\
.ace-vwml-crimson-editor .ace_identifier {\
color: black;\
}\
.ace-vwml-crimson-editor .ace_keyword {\
color: blue;\
}\
.ace-vwml-crimson-editor .ace_constant.ace_buildin {\
color: rgb(88, 72, 246);\
}\
.ace-vwml-crimson-editor .ace_constant.ace_language {\
color: rgb(255, 156, 0);\
}\
.ace-vwml-crimson-editor .ace_constant.ace_library {\
color: rgb(6, 150, 14);\
}\
.ace-vwml-crimson-editor .ace_invalid {\
text-decoration: line-through;\
color: rgb(224, 0, 0);\
}\
.ace-vwml-crimson-editor .ace_fold {\
}\
.ace-vwml-crimson-editor .ace_support.ace_function {\
color: rgb(192, 0, 0);\
}\
.ace-vwml-crimson-editor .ace_support.ace_constant {\
color: rgb(6, 150, 14);\
}\
.ace-vwml-crimson-editor .ace_support.ace_type,\
.ace-vwml-crimson-editor .ace_support.ace_class {\
color: rgb(109, 121, 222);\
}\
.ace-vwml-crimson-editor .ace_keyword.ace_operator {\
color: rgb(49, 132, 149);\
}\
.ace-vwml-crimson-editor .ace_string {\
color: rgb(128, 0, 128);\
}\
.ace-vwml-crimson-editor .ace_comment {\
color: rgb(76, 136, 107);\
}\
.ace-vwml-crimson-editor .ace_comment.ace_doc {\
color: rgb(0, 102, 255);\
}\
.ace-vwml-crimson-editor .ace_comment.ace_doc.ace_tag {\
color: rgb(128, 159, 191);\
}\
.ace-vwml-crimson-editor .ace_constant.ace_numeric {\
color: rgb(0, 0, 64);\
}\
.ace-vwml-crimson-editor .ace_variable {\
color: rgb(0, 64, 128);\
}\
.ace-vwml-crimson-editor .ace_xml-pe {\
color: rgb(104, 104, 91);\
}\
.ace-vwml-crimson-editor .ace_marker-layer .ace_selection {\
background: rgb(181, 213, 255);\
}\
.ace-vwml-crimson-editor .ace_marker-layer .ace_step {\
background: rgb(252, 255, 0);\
}\
.ace-vwml-crimson-editor .ace_marker-layer .ace_stack {\
background: rgb(164, 229, 101);\
}\
.ace-vwml-crimson-editor .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid rgb(192, 192, 192);\
}\
.ace-vwml-crimson-editor .ace_marker-layer .ace_active-line {\
background: rgb(232, 242, 254);\
}\
.ace-vwml-crimson-editor .ace_gutter-active-line {\
background-color : #dcdcdc;\
}\
.ace-vwml-crimson-editor .ace_meta.ace_tag {\
color:rgb(28, 2, 255);\
}\
.ace-vwml-crimson-editor .ace_marker-layer .ace_selected-word {\
background: rgb(250, 250, 255);\
border: 1px solid rgb(200, 200, 250);\
}\
.ace-vwml-crimson-editor .ace_string.ace_regex {\
color: rgb(192, 0, 192);\
}\
.ace-vwml-crimson-editor .ace_lparen_round,\
.ace-vwml-crimson-editor .ace_rparen_round {\
color: #60F568\
}\
.ace-vwml-crimson-editor .ace_lparen_figure,\
.ace-vwml-crimson-editor .ace_rparen_figure {\
color: #197DFF\
}\
.ace-vwml-crimson-editor .ace_lparen_square,\
.ace-vwml-crimson-editor .ace_rparen_square {\
color: #FF4167\
}\
.ace-vwml-crimson-editor .ace_vwml_directives {\
color: #F59B35\
}\
.ace-vwml-crimson-editor .ace_vwml_langs {\
color: #FA69FF\
}\
.ace-vwml-crimson-editor .ace_indent-guide {\
background: url(\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bLly//BwAmVgd1/w11/gAAAABJRU5ErkJggg==\") right repeat-y;\
}";

exports.cssClass = "ace-vwml-crimson-editor";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});
