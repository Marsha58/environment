define("ace/theme/vwml_chrome",["require","exports","module","ace/lib/dom"], function(require, exports, module) {

exports.isDark = false;
exports.cssClass = "ace-vwml-chrome";
exports.cssText = ".ace-vwml-chrome .ace_gutter {\
background: #ebebeb;\
color: #333;\
overflow : hidden;\
}\
.ace-vwml-chrome .ace_print-margin {\
width: 1px;\
background: #e8e8e8;\
}\
.ace-vwml-chrome {\
background-color: #FFFFFF;\
color: black;\
}\
.ace-vwml-chrome .ace_cursor {\
color: black;\
}\
.ace-vwml-chrome .ace_invisible {\
color: rgb(191, 191, 191);\
}\
.ace-vwml-chrome .ace_constant.ace_buildin {\
color: rgb(88, 72, 246);\
}\
.ace-vwml-chrome .ace_constant.ace_language {\
color: rgb(88, 92, 246);\
}\
.ace-vwml-chrome .ace_constant.ace_library {\
color: rgb(6, 150, 14);\
}\
.ace-vwml-chrome .ace_invalid {\
background-color: rgb(153, 0, 0);\
color: white;\
}\
.ace-vwml-chrome .ace_fold {\
}\
.ace-vwml-chrome .ace_support.ace_function {\
color: rgb(60, 76, 114);\
}\
.ace-vwml-chrome .ace_support.ace_constant {\
color: rgb(6, 150, 14);\
}\
.ace-vwml-chrome .ace_lparen_round,\
.ace-vwml-chrome .ace_rparen_round {\
color: #A35D0D\
}\
.ace-vwml-chrome .ace_lparen_figure,\
.ace-vwml-chrome .ace_rparen_figure {\
color: #7433A3\
}\
.ace-vwml-chrome .ace_lparen_square,\
.ace-vwml-chrome .ace_rparen_square {\
color: #FF4167\
}\
.ace-vwml-chrome .ace_vwml_directives {\
color: #F59B35\
}\
.ace-vwml-chrome .ace_vwml_langs {\
color: #FA69FF\
}\
.ace-vwml-chrome .ace_support.ace_type,\
.ace-vwml-chrome .ace_support.ace_class\
.ace-vwml-chrome .ace_support.ace_other {\
color: rgb(109, 121, 222);\
}\
.ace-vwml-chrome .ace_variable.ace_parameter {\
font-style:italic;\
color:#FD971F;\
}\
.ace-vwml-chrome .ace_keyword.ace_operator {\
color: rgb(104, 118, 135);\
}\
.ace-vwml-chrome .ace_comment {\
color: #236e24;\
}\
.ace-vwml-chrome .ace_comment.ace_doc {\
color: #236e24;\
}\
.ace-vwml-chrome .ace_comment.ace_doc.ace_tag {\
color: #236e24;\
}\
.ace-vwml-chrome .ace_constant.ace_numeric {\
color: rgb(0, 0, 205);\
}\
.ace-vwml-chrome .ace_variable {\
color: rgb(49, 132, 149);\
}\
.ace-vwml-chrome .ace_xml-pe {\
color: rgb(104, 104, 91);\
}\
.ace-vwml-chrome .ace_entity.ace_name.ace_function {\
color: #0000A2;\
}\
.ace-vwml-chrome .ace_heading {\
color: rgb(12, 7, 255);\
}\
.ace-vwml-chrome .ace_list {\
color:rgb(185, 6, 144);\
}\
.ace-vwml-chrome .ace_marker-layer .ace_selection {\
background: rgb(181, 213, 255);\
}\
.ace-vwml-chrome .ace_marker-layer .ace_step {\
background: rgb(252, 255, 0);\
}\
.ace-vwml-chrome .ace_marker-layer .ace_stack {\
background: rgb(164, 229, 101);\
}\
.ace-vwml-chrome .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid rgb(192, 192, 192);\
}\
.ace-vwml-chrome .ace_marker-layer .ace_active-line {\
background: rgba(0, 0, 0, 0.07);\
}\
.ace-vwml-chrome .ace_gutter-active-line {\
background-color : #dcdcdc;\
}\
.ace-vwml-chrome .ace_marker-layer .ace_selected-word {\
background: rgb(250, 250, 255);\
border: 1px solid rgb(200, 200, 250);\
}\
.ace-vwml-chrome .ace_storage,\
.ace-vwml-chrome .ace_keyword,\
.ace-vwml-chrome .ace_meta.ace_tag {\
color: rgb(147, 15, 128);\
}\
.ace-vwml-chrome .ace_string.ace_regex {\
color: rgb(255, 0, 0)\
}\
.ace-vwml-chrome .ace_string {\
color: #1A1AA6;\
}\
.ace-vwml-chrome .ace_entity.ace_other.ace_attribute-name {\
color: #994409;\
}\
.ace-vwml-chrome .ace_indent-guide {\
background: url(\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bLly//BwAmVgd1/w11/gAAAABJRU5ErkJggg==\") right repeat-y;\
}\
";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});
