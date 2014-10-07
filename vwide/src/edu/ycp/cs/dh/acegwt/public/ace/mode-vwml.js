define("ace/mode/doc_comment_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var DocCommentHighlightRules = function() {

    this.$rules = {
        "start" : [ {
            token : "comment.doc.tag",
            regex : "@[\\w\\d_]+" // TODO: fix email addresses
        }, {
            token : "comment.doc.tag",
            regex : "\\bTODO\\b"
        }, {
            defaultToken : "comment.doc"
        }]
    };
};

oop.inherits(DocCommentHighlightRules, TextHighlightRules);

DocCommentHighlightRules.getStartRule = function(start) {
    return {
        token : "comment.doc", // doc comment
        regex : "\\/\\*(?=\\*)",
        next  : start
    };
};

DocCommentHighlightRules.getEndRule = function (start) {
    return {
        token : "comment.doc", // closing comment
        regex : "\\*\\/",
        next  : start
    };
};


exports.DocCommentHighlightRules = DocCommentHighlightRules;

});

define("ace/mode/vwml_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/doc_comment_highlight_rules","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var DocCommentHighlightRules = require("./doc_comment_highlight_rules").DocCommentHighlightRules;
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var VwmlHighlightRules = function() {

    var keywords = (
    "Activate|Begin|Born|Bp|CallP|Cartesian|Context|Clone|Do|Eq|Exe|Find|First|ForEach|Gate|Get|ias|Ident|In|include|Intersect|Interrupt|" +
    "Join|Last|lifeterm|Projection|Random|Recall|Relax|Repeat|Rest|Size|Squeeze|Substruct|->|~|^|:|[|]|");

    var buildinConstants = ("null|nil|Infinity|NaN|undefined");
    var languageConstants = ("__java__|__c__|__cpp__|__objective_c__");
    var directivesConstants = ("#if (debug)|#if|#else|#endif");
    var keywordMapper = this.createKeywordMapper({
        "variable.language": "this",
        "keyword": keywords,
        "constant.language": buildinConstants,
        "language.constants": languageConstants,
        "directives" : directivesConstants
    }, "identifier");

    this.$rules = {
        "start" : [
            {
                token : "comment",
                regex : "\\/\\/.*$"
            },
            DocCommentHighlightRules.getStartRule("doc-start"),
            {
                token : "comment", // multi line comment
                regex : "\\/\\*",
                next : "comment"
            }, {
                token : "string", // single line
                regex : '["](?:(?:\\\\.)|(?:[^"\\\\]))*?["]'
            }, {
                token : "string", // single line
                regex : "['](?:(?:\\\\.)|(?:[^'\\\\]))*?[']"
            }, {
                token : "constant.numeric", // hex
                regex : "0[xX][0-9a-fA-F]+\\b"
            }, {
                token : "constant.numeric", // float
                regex : "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"
            }, {
                token : "constant.language.boolean",
                regex : "(?:true|false)\\b"
            }, {
                token : keywordMapper,
                regex : "[a-zA-Z$][a-zA-Z0-9_$]*\\b"
            }, {
                token : "keyword.operator",
                regex : "!|\\^|\\$|%|&|\\*|\\-\\-|\\-|\\+\\+|\\+|~|===|==|=|!=|!==|<=|>=|<<=|>>=|>>>=|<>|<|>|!|&&|\\|\\||\\?\\:|\\*=|%=|\\+=|\\-=|&=|\\^=|\\b(?:in|instanceof|new|delete|typeof|void)"
            }, {
                token : "lparen_figure",
                regex : "[{]"
            }, {
                token : "rparen_figure",
                regex : "[}]"
            },{
                token : "lparen_round",
                regex : "[(]"
            }, {
                token : "rparen_round",
                regex : "[)]"
            }, {
                token : "lparen_square",
                regex : "[[]"
            }, {
                token : "rparen_square",
                regex : "[\\]]"
            },   {
                token : "vwml_directives",
                regex : /(#if\s\(debug\)|#else|#endif|#if)/
            },  {
                token : "vwml_langs",
//                regex : /__[a-z]+[_]*[a-z]+__/
//                regex : /__java__|__c__|__cpp__|__objective_c__/   // this approach works
                  regex : languageConstants    // and it works
            }, {
                token : "text",
                regex : "\\s+"
            }
        ],
        "comment" : [
            {
                token : "comment", // closing comment
                regex : ".*?\\*\\/",
                next : "start"
            }, {
                token : "comment", // comment spanning whole line
                regex : ".+"
            }
        ]
    };

    this.embedRules(DocCommentHighlightRules, "doc-",
        [ DocCommentHighlightRules.getEndRule("start") ]);
};

oop.inherits(VwmlHighlightRules, TextHighlightRules);

exports.VwmlHighlightRules = VwmlHighlightRules;
});

define("ace/mode/vwml",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/vwml_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextMode = require("./text").Mode;
var VwmlHighlightRules = require("./vwml_highlight_rules").VwmlHighlightRules;

var Mode = function() {
    TextMode.call(this);
    this.HighlightRules = VwmlHighlightRules;
};
oop.inherits(Mode, TextMode);

(function() {
    
    this.createWorker = function(session) {
        return null;
    };

    this.$id = "ace/mode/vwml";
}).call(Mode.prototype);

exports.Mode = Mode;
});
