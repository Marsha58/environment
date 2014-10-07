define("ace/theme/vwml_xcode",["require","exports","module","ace/lib/dom"], function(require, exports, module) {

exports.isDark = false;
exports.cssClass = "ace-vwml-xcode";
exports.cssText = "\
.ace-vwml-xcode .ace_gutter {\
background: #e8e8e8;\
color: #333\
}\
.ace-vwml-xcode .ace_print-margin {\
width: 1px;\
background: #e8e8e8\
}\
.ace-vwml-xcode {\
background-color: #FFFFFF;\
color: #000000\
}\
.ace-vwml-xcode .ace_cursor {\
color: #000000\
}\
.ace-vwml-xcode .ace_marker-layer .ace_selection {\
background: #B5D5FF\
}\
.ace-vwml-xcode.ace_multiselect .ace_selection.ace_start {\
box-shadow: 0 0 3px 0px #FFFFFF;\
border-radius: 2px\
}\
.ace-vwml-xcode .ace_marker-layer .ace_step {\
background: rgb(198, 219, 174)\
}\
.ace-vwml-xcode .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid #BFBFBF\
}\
.ace-vwml-xcode .ace_marker-layer .ace_active-line {\
background: rgba(0, 0, 0, 0.071)\
}\
.ace-vwml-xcode .ace_gutter-active-line {\
background-color: rgba(0, 0, 0, 0.071)\
}\
.ace-vwml-xcode .ace_marker-layer .ace_selected-word {\
border: 1px solid #B5D5FF\
}\
.ace-vwml-xcode .ace_constant.ace_language,\
.ace-vwml-xcode .ace_keyword,\
.ace-vwml-xcode .ace_meta,\
.ace-vwml-xcode .ace_variable.ace_language {\
color: #C800A4\
}\
.ace-vwml-xcode .ace_invisible {\
color: #BFBFBF\
}\
.ace-vwml-xcode .ace_constant.ace_character,\
.ace-vwml-xcode .ace_constant.ace_other {\
color: #275A5E\
}\
.ace-vwml-xcode .ace_constant.ace_numeric {\
color: #3A00DC\
}\
.ace-vwml-xcode .ace_entity.ace_other.ace_attribute-name,\
.ace-vwml-xcode .ace_support.ace_constant,\
.ace-vwml-xcode .ace_support.ace_function {\
color: #450084\
}\
.ace-vwml-xcode .ace_fold {\
background-color: #C800A4;\
border-color: #000000\
}\
.ace-vwml-xcode .ace_entity.ace_name.ace_tag,\
.ace-vwml-xcode .ace_support.ace_class,\
.ace-vwml-xcode .ace_support.ace_type {\
color: #790EAD\
}\
.ace-vwml-xcode .ace_storage {\
color: #C900A4\
}\
.ace-vwml-xcode .ace_string {\
color: #DF0002\
}\
.ace-vwml-xcode .ace_comment {\
color: #008E00\
}\
.ace-vwml-xcode .ace_lparen_round,\
.ace-vwml-xcode .ace_rparen_round {\
color: #60F568\
}\
.ace-vwml-xcode .ace_lparen_figure,\
.ace-vwml-xcode .ace_rparen_figure {\
color: #197DFF\
}\
.ace-vwml-xcode .ace_lparen_square,\
.ace-vwml-xcode .ace_rparen_square {\
color: #FF4167\
}\
.ace-vwml-xcode .ace_vwml_directives {\
color: #F59B35\
}\
.ace-vwml-xcode .ace_vwml_langs {\
color: #FA69FF\
}\
.ace-vwml-xcode .ace_indent-guide {\
background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bLly//BwAmVgd1/w11/gAAAABJRU5ErkJggg==) right repeat-y\
}";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});
