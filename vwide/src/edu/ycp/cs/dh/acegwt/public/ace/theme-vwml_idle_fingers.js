define("ace/theme/vwml_idle_fingers",["require","exports","module","ace/lib/dom"], function(require, exports, module) {

exports.isDark = true;
exports.cssClass = "ace-vwml-idle-fingers";
exports.cssText = ".ace-vwml-idle-fingers .ace_gutter {\
background: #3b3b3b;\
color: #fff\
}\
.ace-vwml-idle-fingers .ace_print-margin {\
width: 1px;\
background: #3b3b3b\
}\
.ace-vwml-idle-fingers {\
background-color: #323232;\
color: #FFFFFF\
}\
.ace-vwml-idle-fingers .ace_cursor {\
color: #91FF00\
}\
.ace-vwml-idle-fingers .ace_marker-layer .ace_selection {\
background: rgba(90, 100, 126, 0.88)\
}\
.ace-vwml-idle-fingers.ace_multiselect .ace_selection.ace_start {\
box-shadow: 0 0 3px 0px #323232;\
border-radius: 2px\
}\
.ace-vwml-idle-fingers .ace_marker-layer .ace_step {\
background: rgb(102, 82, 0)\
}\
.ace-vwml-idle-fingers .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid #404040\
}\
.ace-vwml-idle-fingers .ace_marker-layer .ace_active-line {\
background: #353637\
}\
.ace-vwml-idle-fingers .ace_gutter-active-line {\
background-color: #353637\
}\
.ace-vwml-idle-fingers .ace_marker-layer .ace_selected-word {\
border: 1px solid rgba(90, 100, 126, 0.88)\
}\
.ace-vwml-idle-fingers .ace_invisible {\
color: #404040\
}\
.ace-vwml-idle-fingers .ace_keyword,\
.ace-vwml-idle-fingers .ace_meta {\
color: #CC7833\
}\
.ace-vwml-idle-fingers .ace_constant,\
.ace-vwml-idle-fingers .ace_constant.ace_character,\
.ace-vwml-idle-fingers .ace_constant.ace_character.ace_escape,\
.ace-vwml-idle-fingers .ace_constant.ace_other,\
.ace-vwml-idle-fingers .ace_support.ace_constant {\
color: #6C99BB\
}\
.ace-vwml-idle-fingers .ace_lparen_round,\
.ace-vwml-idle-fingers .ace_rparen_round {\
color: #60F568\
}\
.ace-vwml-idle-fingers .ace_lparen_figure,\
.ace-vwml-idle-fingers .ace_rparen_figure {\
color: #197DFF\
}\
.ace-vwml-idle-fingers .ace_lparen_square,\
.ace-vwml-idle-fingers .ace_rparen_square {\
color: #FF4167\
}\
.ace-vwml-idle-fingers .ace_vwml_directives {\
color: #F59B35\
}\
.ace-vwml-idle-fingers .ace_vwml_langs {\
color: #FF00FF\
}\
.ace-vwml-idle-fingers .ace_invalid {\
color: #FFFFFF;\
background-color: #FF0000\
}\
.ace-vwml-idle-fingers .ace_fold {\
background-color: #CC7833;\
border-color: #FFFFFF\
}\
.ace-vwml-idle-fingers .ace_support.ace_function {\
color: #B83426\
}\
.ace-vwml-idle-fingers .ace_variable.ace_parameter {\
font-style: italic\
}\
.ace-vwml-idle-fingers .ace_string {\
color: #A5C261\
}\
.ace-vwml-idle-fingers .ace_string.ace_regexp {\
color: #CCCC33\
}\
.ace-vwml-idle-fingers .ace_comment {\
font-style: italic;\
color: #BC9458\
}\
.ace-vwml-idle-fingers .ace_meta.ace_tag {\
color: #FFE5BB\
}\
.ace-vwml-idle-fingers .ace_entity.ace_name {\
color: #FFC66D\
}\
.ace-vwml-idle-fingers .ace_collab.ace_user1 {\
color: #323232;\
background-color: #FFF980\
}\
.ace-vwml-idle-fingers .ace_indent-guide {\
background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAEklEQVQImWMwMjLyZYiPj/8PAAreAwAI1+g0AAAAAElFTkSuQmCC) right repeat-y\
}";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});
