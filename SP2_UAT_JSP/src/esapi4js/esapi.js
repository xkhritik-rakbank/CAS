/*
 * OWASP Enterprise Security API (ESAPI)
 *
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * <a href="http://www.owasp.org/index.php/ESAPI">http://www.owasp.org/index.php/ESAPI</a>.
 *
 * Copyright (c) 2008 - The OWASP Foundation
 *
 * The ESAPI is published by OWASP under the BSD license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 */

// Utility and Core API Methods
var $namespace = function(name, separator, container){
  var ns = name.split(separator || '.'),
    o = container || window,
    i,
    len;
  for(i = 0, len = ns.length; i < len; i++){
    o = o[ns[i]] = o[ns[i]] || {};
  }
  return o;
};

var $require = function( sPackage ) {
    if ( !eval(sPackage) ) {
        throw new RuntimeException( sPackage + " required, but does not exist in the document." );
    }
};

if (!$) {
    var $ = function( sElementID ) {
        return document.getElementById( sElementID );
    };
}

if (!Array.prototype.each) {
    Array.prototype.each = function(fIterator) {
        if (typeof fIterator != 'function') {
            throw 'Illegal Argument for Array.each';
        }

        for (var i = 0; i < this.length; i ++) {
            fIterator(this[i]);
        }
    };
}

if (!Array.prototype.contains) {
    Array.prototype.contains = function(srch) {
        var found = false;
        this.each(function(e) {
            if ( ( srch.equals && srch.equals(e) ) || e == srch) {
                found = true;
                return;
            }
        });
        return found;
    };
}

if (!Array.prototype.containsKey) {
    Array.prototype.containsKey = function(srch) {
        for ( var key in this ) {
            if ( key.toLowerCase() == srch.toLowerCase() ) {
                return true;
            }
        }
        return false;
    };
}

if (!Array.prototype.getCaseInsensitive) {
    Array.prototype.getCaseInsensitive = function(key) {
        for (var k in this) {
            if (k.toLowerCase() == key.toLowerCase()) {
                return this[k];
            }
        }
        return null;
    };
}

if (!String.prototype.charCodeAt) {
    String.prototype.charCodeAt = function( idx ) {
        var c = this.charAt(idx);
        for ( var i=0;i<65536;i++) {
            var s = String.fromCharCode(i);
            if ( s == c ) { return i; }
        }
        return 0;
    };
}
             
if (!String.prototype.endsWith) {
    String.prototype.endsWith = function( test ) {
        return this.substr( ( this.length - test.length ), test.length ) == test;
    };
}

// Declare Core Exceptions
if ( !Exception ) {
    var Exception = function( sMsg, oException ) {
        this.cause = oException;
        this.errorMessage = sMsg;
    };

    Exception.prototype = Error.prototype;

    Exception.prototype.getCause = function() { return this.cause; };

    Exception.prototype.getMessage = function() { return this.message; };

    /**
     * This method creates the stacktrace for the Exception only when it is called the first time and
     * caches it for access after that. Since building a stacktrace is a fairly expensive process, we
     * only want to do it if it is called.
     */
    Exception.prototype.getStackTrace = function() {
        if ( this.callstack ) {
            return this.callstack;
        }

        if ( this.stack ) { // Mozilla
            var lines = stack.split("\n");
            for ( var i=0, len=lines.length; i<len; i ++ ) {
                if ( lines[i].match( /^\s*[A-Za-z0-9\=+\$]+\(/ ) ) {
                    this.callstack.push(lines[i]);
                }
            }
            this.callstack.shift();
            return this.callstack;
        }
        else if ( window.opera && this.message ) { // Opera
            var lines = this.message.split('\n');
            for ( var i=0, len=lines.length; i<len; i++ ) {
                if ( lines[i].match( /^\s*[A-Za-z0-9\=+\$]+\(/ ) ) {
                    var entry = lines[i];
                    if ( lines[i+1] ) {
                        entry += " at " + lines[i+1];
                        i++;
                    }
                    this.callstack.push(entry);
                }
            }
            this.callstack.shift();
            return this.callstack;
        }
        else { // IE and Safari
            var currentFunction = arguments.callee.caller;
            while ( currentFunction ) {
                var fn = currentFunction.toString();
                var fname = fn.substring(fn.indexOf("function")+8,fn.indexOf("(")) || "anonymous";
                this.callstack.push(fname);
                currentFunction = currentFunction.caller;
            }
            return this.callstack;
        }
    };

    Exception.prototype.printStackTrace = function( writer ) {
        var out = this.constructor.toString() + ": " + this.getMessage() + "|||" + this.getStackTrace().join( "|||" );

        if ( this.cause ) {
            if ( this.cause.printStackTrace ) {
                out += "||||||Caused by " + this.cause.printStackTrace().replace( "\n", "|||" );
            }
        }

        if ( !writer ) {
            return writer.replace( "|||", "\n" );
        } else if ( writer.value ) {
            writer.value = out.replace( "|||", "\n" );
        } else if ( writer.writeln ) {
            writer.writeln( out.replace( "|||", "\n" ) );
        } else if ( writer.innerHTML ) {
            writer.innerHTML = out.replace( "|||", "<br/>" );
        } else if ( writer.innerText ) {
            writer.innerText = out.replace( "|||", "<br/>" );
        } else if ( writer.append ) {
            writer.append( out.replace( "|||", "\n" ) );
        } else if ( writer instanceof Function ) {
            writer(out.replace( "|||", "\n" ) );
        }
    };
}

if ( !RuntimeException ) {
    var RuntimeException = {};
    RuntimeException.prototype = Exception.prototype;
}

if ( !IllegalArgumentException ) {
    var IllegalArgumentException = {};
    IllegalArgumentException.prototype = Exception.prototype;
}/*
 * OWASP Enterprise Security API (ESAPI)
 *
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * <a href="http://www.owasp.org/index.php/ESAPI">http://www.owasp.org/index.php/ESAPI</a>.
 *
 * Copyright (c) 2008 - The OWASP Foundation
 *
 * The ESAPI is published by OWASP under the BSD license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 */

var $ESAPI_Properties = {
    logging: {
        Implementation: 'org.owasp.esapi.reference.logging.Log4JSLogFactory',
        Level: 'org.owasp.esapi.Logger.ALL',
        Appenders: [ new Log4js.ConsoleAppender() ]
    },

    encoder: {
        Implementation: 'org.owasp.esapi.reference.encoding.DefaultEncoder',
        AllowMultipleEncoding: false
    },

    validation: {
        Implementation: 'org.owasp.esapi.validators.DefaultValidator',
        AccountName: '^[a-zA-Z0-9]{3,20}$',
        SafeString: '[a-zA-Z0-9\\-_+]*',
        Email: '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$',
        IPAddress: '^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$',
        URL: '^(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\:\\\'\\/\\\\\\+=&amp;%\\$#_]*)?$',
        CreditCard: '^(\\d{4}[- ]?){3}\\d{4}$',
        SSN: '^(?!000)([0-6]\\d{2}|7([0-6]\\d|7[012]))([ -]?)(?!00)\\d\\d\\3(?!0000)\\d{4}$'
    }
};/*
 * OWASP Enterprise Security API (ESAPI)
 *
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * <a href="http://www.owasp.org/index.php/ESAPI">http://www.owasp.org/index.php/ESAPI</a>.
 *
 * Copyright (c) 2008 - The OWASP Foundation
 *
 * The ESAPI is published by OWASP under the BSD license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 */

$namespace('org.owasp.esapi');
$require('$ESAPI_Properties');

org.owasp.esapi = {
    Encoder: function() {

    },

    EnterpriseSecurityException: function(sUserMessage, sLogMessage, oException) {
        var _logMessage = sLogMessage;
        var _super = new Exception(sUserMessage, oException);

        return {
            getMessage: _super.getMessage(),
            getUserMessage: _super.getMessage,
            getLogMessage: function() {
                return _logMessage;
            },
            getStackTrace: _super.getStackTrace,
            printStackTrace: _super.printStackTrace
        };
    },

    HTTPUtilities: function() {
        return {
            addCookie: false,
            getSessionID: false,
            getCookie: false,
            killAllCookies: false,
            killCookie: false,
            logHTTPRequest: false,
            sendForward: false,
            getRequestParameter: false
        };
    },

    IntrusionException: function(sUserMessage, sLogMessage, oCause) {
        var _super = new org.owasp.esapi.EnterpriseSecurityException(sUserMessage, sLogMessage, oCause);

        return {
            getMessage: _super.getMessage,
            getUserMessage: _super.getMessage,
            getLogMessage: _super.getLogMessage,
            getStackTrace: _super.getStackTrace,
            printStackTrace: _super.printStackTrace
        };
    },

    LogFactory: function() {
        return {
            getLogger: false
        };
    },

    Logger: {
        EventType: function( sName, bNewSuccess ) {
            var type = sName;
            var success = bNewSuccess;

            return {
                isSuccess: function() {
                    return success;
                },

                toString: function() {
                    return type;
                }
            };
        },

        OFF: Number.MAX_VALUE,
        FATAL: 1000,
        ERROR: 800,
        WARNING: 600,
        INFO: 400,
        DEBUG: 200,
        TRACE: 100,
        ALL: Number.MIN_VALUE,

        prototype: {
            setLevel: false,
            fatal: false,
            error: false,
            isErrorEnabled: false,
            warning: false,
            isWarningEnabled: false,
            info: false,
            isInfoEnabled: false,
            debug: false,
            isDebugEnabled: false,
            trace: false,
            isTraceEnabled: false
        }
    },

    PreparedString: function(sTemplate, oCodec, sParameterCharacter) {
        // Private Scope
        var parts = [];
        var parameters = [];

        function split(s) {
            var idx = 0, pcount = 0;
            for (var i = 0; i < s.length; i ++) {
                if (s.charAt(i) == sParameterCharacter) {
                    pcount ++;
                    parts.push(s.substr(idx, i));
                    idx = i + 1;
                }
            }
            parts.push(s.substr(idx));
            parameters = new Array(pcount);
        }

        ;

        if (!sParameterCharacter) {
            sParameterCharacter = '?';
        }

        split(sTemplate);

        return {
            set: function(iIndex, sValue, codec) {
                if (iIndex < 1 || iIndex > parameters.length) {
                    throw new IllegalArgumentException("Attempt to set parameter: " + iIndex + " on a PreparedString with only " + parameters.length + " placeholders");
                }
                if (!codec) {
                    codec = oCodec;
                }
                parameters[iIndex - 1] = codec.encode([], sValue);
            },

            toString: function() {
                for (var ix = 0; ix < parameters.length; ix ++) {
                    if (parameters[ix] == null) {
                        throw new RuntimeException("Attempt to render PreparedString without setting parameter " + (ix + 1));
                    }
                }
                var out = '', i = 0;
                for (var p = 0; p < parts.length; p ++) {
                    out += parts[p];
                    if (i < parameters.length) {
                        out += parameters[i++];
                    }
                }
                return out;
            }
        };
    },

    ValidationErrorList: function() {
        var errorList = Array();

        return {
            addError: function( sContext, oValidationException ) {
                if ( sContext == null ) throw new RuntimeException( "Context cannot be null: " + oValidationException.getLogMessage(), oValidationException );
                if ( oValidationException == null ) throw new RuntimeException( "Context (" + sContext + ") - Error cannot be null" );
                if ( errorList[sContext] ) throw new RuntimeException( "Context (" + sContext + ") already exists. must be unique." );
                errorList[sContext] = oValidationException;
            },

            errors: function() {
                return errorList;
            },

            isEmpty: function() {
                return errorList.length == 0;
            },

            size: function() {
                return errorList.length;
            }
        };
    },

    ValidationRule: function() {
        return {
            getValid: false,
            setAllowNull: false,
            getTypeName: false,
            setTypeName: false,
            setEncoder: false,
            assertValid: false,
            getValid: false,
            getSafe: false,
            isValid: false,
            whitelist: false
        };
    },

    Validator: function() {
        return {
            addRule: false,
            getRule: false,
            getValidInput: false,
            isValidDate: false,
            getValidDate: false,
            isValidSafeHTML: false,
            getValidSafeHTML: false,
            isValidCreditCard: false,
            getValidCreditCard: false,
            isValidFilename: false,
            getValidFilename: false,
            isValidNumber: false,
            getValidNumber: false,
            isValidPrintable: false,
            getValidPrintable: false
        };
    },

    ESAPI: function() {
        var _properties = $ESAPI_Properties;

        var _encoder = null;
        var _validator = null;
        var _logFactory = null;

        return {
            properties: _properties,

            encoder: function() {
                $require(_properties.encoder.Implementation);
                if (!_encoder) {
                    eval('_encoder = new ' + _properties.encoder.Implementation + '();');
                }
                return _encoder;
            },

            logFactory: function() {
                eval('$require('+_properties.logging.Implementation+');');
                if ( !_logFactory ) {
                    eval("_logFactory = new " + _properties.logging.Implementation + "();" );
                }
                return _logFactory;
            },

            logger: function(sModuleName) {
                return this.logFactory().getLogger(sModuleName);
            },

            validator: function() {
                $require(_properties.validation.Implementation);
                if (_validator == null) {
                    eval('_validator = new ' + _properties.validation.Implementation + '();');
                }
                return _validator;
            }
        };
    }
};

var $ESAPI = null;

var ESAPI_Initialize = function() {
    $ESAPI = new org.owasp.esapi.ESAPI();
};/*
 * OWASP Enterprise Security API (ESAPI)
 *
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * <a href="http://www.owasp.org/index.php/ESAPI">http://www.owasp.org/index.php/ESAPI</a>.
 *
 * Copyright (c) 2008 - The OWASP Foundation
 *
 * The ESAPI is published by OWASP under the BSD license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 */

$namespace('org.owasp.esapi.codecs');

org.owasp.esapi.codecs = {
    PushbackString: function(sInput) {
        var _input = sInput,
                _pushback = '',
                _temp = '',
                _index = 0,
                _mark = 0;

        return {
            pushback: function(c) {
                _pushback = c;
            },

            index: function() {
                return _index;
            },

            hasNext: function() {
                if (_pushback != null) return true;
                return !(_input == null || _input.length == 0 || _index >= _input.length);

            },

            next: function() {
                if (_pushback != null) {
                    var save = _pushback;
                    _pushback = null;
                    return save;
                }
                if (_input == null || _input.length == 0 || _index >= _input.length) {
                    return null;
                }
                return _input.charAt(_index++);
            },

            nextHex: function() {
                var c = this.next();
                if (this.isHexDigit(c)) return c;
                return null;
            },

            nextOctal: function() {
                var c = this.next();
                if (this.isOctalDigit(c)) return c;
                return null;
            },

            isHexDigit: function(c) {
                return c != null && ( ( c >= '0' && c <= '9' ) || ( c >= 'a' && c <= 'f' ) || ( c >= 'A' && c <= 'F' ) );
            },

            isOctalDigit: function(c) {
                return c != null && ( c >= '0' && c <= '7' );
            },

            peek: function(c) {
                if (!c) {
                    if (_pushback != null) return _pushback;
                    if (_input == null || _input.length == 0 || _index >= _input.length) return null;
                    return _input.charAt(_index);
                } else {
                    if (_pushback != null && _pushback == c) return true;
                    if (_input == null || _input.length == 0 || _index >= _input.length) return false;
                    return _input.charAt(_index) == c;
                }
            },

            mark: function() {
                _temp = _pushback;
                _mark = _index;
            },

            reset: function() {
                _pushback = _temp;
                _index = _mark;
            },

            remainder: function() {
                var out = _input.substr(_index);
                if (_pushback != null) {
                    out = _pushback + out;
                }
                return out;
            }
        };
    },

    Base64: {
        _keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

        encode: function(sInput) {
            if (!sInput) {
                return null;
            }

            var out = '';
            var ch1,ch2,ch3,enc1,enc2,enc3,enc4;
            var i = 0;

            var input = org.owasp.esapi.codecs.UTF8.encode(sInput);

            while (i < input.length) {
                ch1 = input.charCodeAt(i++);
                ch2 = input.charCodeAt(i++);
                ch3 = input.charCodeAt(i++);

                enc1 = ch1 >> 2;
                enc2 = ((ch1 & 3) << 4) | (ch2 >> 4);
                enc3 = ((ch2 & 15) << 2) | (ch3 >> 6);
                enc4 = ch3 & 63;

                if (isNaN(ch2)) {
                    enc3 = enc4 = 64;
                }
                else if (isNaN(ch3)) {
                    enc4 = 64;
                }

                out += this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
            }

            return out;
        },

        decode: function(sInput) {
            if (!sInput) {
                return null;
            }

            var out = '';
            var ch1, ch2, ch3, enc1, enc2, enc3, enc4;
            var i = 0;

            var input = sInput.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            while (i < input.length) {
                enc1 = this._keyStr.indexOf(input.charAt(i++));
                enc2 = this._keyStr.indexOf(input.charAt(i++));
                enc3 = this._keyStr.indexOf(input.charAt(i++));
                enc4 = this._keyStr.indexOf(input.charAt(i++));

                ch1 = (enc1 << 2) | (enc2 >> 4);
                ch2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                ch3 = ((enc3 & 3) << 6) | enc4;

                out += String.fromCharCode(ch1);
                if (enc3 != 64) {
                    out += String.fromCharCode(ch2);
                }
                if (enc4 != 64) {
                    out += String.fromCharCode(ch3);
                }
            }

            out = org.owasp.esapi.codecs.UTF8.decode(out);
            return out;
        }
    },

    Codec: function() {
        return {
            /**
             * Encode a String so that it can be safely used in a specific context.
             *
             * @param aImmune
             *              array of immune characters
             * @param sInput
             *              the String to encode
             * @return the encoded String
             */
            encode: function(aImmune, sInput) {
                var out = '';
                for (var i = 0; i < sInput.length; i ++) {
                    var c = sInput.charAt(i);
                    out += this.encodeCharacter(aImmune, c);
                }
                return out;
            },

            /**
             * Default implementation that should be overridden in specific codecs.
             *
             * @param aImmune
             *              array of immune characters
             * @param c
             *              the Character to encode
             * @return
             *              the encoded Character
             */
            encodeCharacter: function(aImmune, c) {
                return c;
            },

            /**
             * Decode a String that was encoded using the encode method in this Class
             *
             * @param sInput
             *              the String to decode
             * @return
             *              the decoded String
             */
            decode: function(sInput) {
                var out = '';
                var pbs = new org.owasp.esapi.codecs.PushbackString(sInput);
                while (pbs.hasNext()) {
                    var c = this.decodeCharacter(pbs);
                    if (c != null) {
                        out += c;
                    } else {
                        out += pbs.next();
                    }
                }
                return out;
            },

            /**
             * Returns the decoded version of the next character from the input string and advances the
             * current character in the PushbackString.  If the current character is not encoded, this
             * method MUST reset the PushbackString.
             *
             * @param oPushbackString the Character to decode
             * @return the decoded Character
             */
            decodeCharacter: function(oPushbackString) {
                return oPushbackString.next();
            }
        };
    },

    getHexForNonAlphanumeric: function(c) {
        if (c.charCodeAt(0) < 256) {
            return org.owasp.esapi.codecs.Codec.hex[c.charCodeAt(0)];
        }
        return c.charCodeAt(0).toString(16);
    },

    HTMLEntityCodec: function() {
        var _super = new org.owasp.esapi.codecs.Codec();

        var getNumericEntity = function(input) {
            var first = input.peek();
            if (first == null) {
                return null;
            }

            if (first == 'x' || first == 'X') {
                input.next();
                return parseHex(input);
            }
            return parseNumber(input);
        };
        
        
        var parseNumber = function(input) {
            var out = '';
            while (input.hasNext()) {
                var c = input.peek();
                if (c.match(/[0-9]/)) {
                    out += c;
                    input.next();
                } else if (c == ';') {
                    input.next();
                    break;
                } else {
                    break;
                }
            }

            try {
                return String.fromCharCode(parseInt(out));
                //Commented to fix esapi bug
                //return parseInt(out);
            } catch (e) {
                return null;
            }
        };

        var parseHex = function(input) {
            var out = '';
            while (input.hasNext()) {
                var c = input.peek();
                if (c.match(/[0-9A-Fa-f]/)) {
                    out += c;
                    input.next();
                } else if (c == ';') {
                    input.next();
                    break;
                } else {
                    break;
                }
            }
            try {
                return String.fromCharCode(parseInt(out, 16));
                //Commented to fix esapi bug
                //return parseInt(out, 16);
            } catch (e) {
                return null;
            }
        };


        /*  var parseNumber = function(input) {
            var out = '';
            while (input.hasNext()) {
                var c = input.peek();
                if (c.match(/[0-9]/)) {
                    out += c;
                    input.next();
                } else if (c == ';') {
                    input.next();
                    break;
                } else {
                    break;
                }
            }

            try {
                return parseInt(out);
            } catch (e) {
                return null;
            }
        };

        var parseHex = function(input) {
            var out = '';
            while (input.hasNext()) {
                var c = input.peek();
                if (c.match(/[0-9A-Fa-f]/)) {
                    out += c;
                    input.next();
                } else if (c == ';') {
                    input.next();
                    break;
                } else {
                    break;
                }
            }
            try {
                return parseInt(out, 16);
            } catch (e) {
                return null;
            }
        };
*/
        
        
        
        var getNamedEntity = function(input) {
            var entity = '';
            while (input.hasNext()) {
                var c = input.peek();
                if (c.match(/[A-Za-z]/)) {
                    entity += c;
                    input.next();
                    if (entityToCharacterMap.containsKey('&' + entity)) {
                        if (input.peek(';')) input.next();
                        break;
                    }
                } else if (c == ';') {
                    input.next();
                } else {
                    break;
                }
            }

            return String.fromCharCode(entityToCharacterMap.getCaseInsensitive('&' + entity));
        };

        return {
            encode: _super.encode,

            decode: _super.decode,

            encodeCharacter: function(aImmune, c) {
                if (aImmune.contains(c)) {
                    return c;
                }

                var hex = org.owasp.esapi.codecs.getHexForNonAlphanumeric(c);
                if (hex == null) {
                    return c;
                }

                var cc = c.charCodeAt(0);
                if (( cc <= 0x1f && c != '\t' && c != '\n' && c != '\r' ) || ( cc >= 0x7f && cc <= 0x9f ) || c == ' ') {
                    return " ";
                }

                var entityName = characterToEntityMap[cc];
                if (entityName != null) {
                    return entityName + ";";
                }

                return "&#x" + hex + ";";
            },

            decodeCharacter: function(oPushbackString) {
                //noinspection UnnecessaryLocalVariableJS
                var input = oPushbackString;
                input.mark();
                var first = input.next();
                if (first == null || first != '&') {
                    input.reset();
                    return null;
                }

                var second = input.next();
                if (second == null) {
                    input.reset();
                    return null;
                }

                if (second == '#') {
                    var c = getNumericEntity(input);
                    if (c != null) {
                        return c;
                    }
                } else if (second.match(/[A-Za-z]/)) {
                    input.pushback(second);
                    c = getNamedEntity(input);
                    if (c != null) {
                        return c;
                    }
                }
                input.reset();
                return null;
            }
        };
    },

    JavascriptCodec: function() {
        var _super = new org.owasp.esapi.codecs.Codec();

        return {
            encode: function(aImmune, sInput) {
                var out = '';
                for (var idx = 0; idx < sInput.length; idx ++) {
                    var ch = sInput.charAt(idx);
                    if (aImmune.contains(ch)) {
                        out += ch;
                    }
                    else {
                        var hex = org.owasp.esapi.codecs.getHexForNonAlphanumeric(ch);
                        if (hex == null) {
                            out += ch;
                        }
                        else {
                            var tmp = ch.charCodeAt(0).toString(16);
                            if (ch.charCodeAt(0) < 256) {
                                var pad = "00".substr(tmp.length);
                                out += "\\x" + pad + tmp.toUpperCase();
                            }
                            else {
                                pad = "0000".substr(tmp.length);
                                out += "\\u" + pad + tmp.toUpperCase();
                            }
                        }
                    }
                }
                return out;
            },

            decode: _super.decode,

            decodeCharacter: function(oPushbackString) {
                oPushbackString.mark();
                var first = oPushbackString.next();
                if (first == null) {
                    oPushbackString.reset();
                    return null;
                }

                if (first != '\\') {
                    oPushbackString.reset();
                    return null;
                }

                var second = oPushbackString.next();
                if (second == null) {
                    oPushbackString.reset();
                    return null;
                }

                // \0 collides with the octal decoder and is non-standard
                // if ( second.charValue() == '0' ) {
                //      return Character.valueOf( (char)0x00 );
                if (second == 'b') {
                    return 0x08;
                } else if (second == 't') {
                    return 0x09;
                } else if (second == 'n') {
                    return 0x0a;
                } else if (second == 'v') {
                    return 0x0b;
                } else if (second == 'f') {
                    return 0x0c;
                } else if (second == 'r') {
                    return 0x0d;
                } else if (second == '\"') {
                    return 0x22;
                } else if (second == '\'') {
                    return 0x27;
                } else if (second == '\\') {
                    return 0x5c;
                } else if (second.toLowerCase() == 'x') {
                    out = '';
                    for (var i = 0; i < 2; i++) {
                        var c = oPushbackString.nextHex();
                        if (c != null) {
                            out += c;
                        } else {
                            input.reset();
                            return null;
                        }
                    }
                    try {
                        n = parseInt(out, 16);
                        return String.fromCharCode(n);
                    } catch (e) {
                        oPushbackString.reset();
                        return null;
                    }
                } else if (second.toLowerCase() == 'u') {
                    out = '';
                    for (i = 0; i < 4; i++) {
                        c = oPushbackString.nextHex();
                        if (c != null) {
                            out += c;
                        } else {
                            input.reset();
                            return null;
                        }
                    }
                    try {
                        var n = parseInt(out, 16);
                        return String.fromCharCode(n);
                    } catch (e) {
                        oPushbackString.reset();
                        return null;
                    }
                } else if (oPushbackString.isOctalDigit(second)) {
                    var out = second;
                    var c2 = oPushbackString.next();
                    if (!oPushbackString.isOctalDigit(c2)) {
                        oPushbackString.pushback(c2);
                    } else {
                        out += c2;
                        var c3 = oPushbackString.next();
                        if (!oPushbackString.isOctalDigit(c3)) {
                            oPushbackString.pushback(c3);
                        } else {
                            out += c3;
                        }
                    }

                    try {
                        n = parseInt(out, 8);
                        return String.fromCharCode(n);
                    } catch (e) {
                        oPushbackString.reset();
                        return null;
                    }
                }
                return second;
            }
        };
    },

    CSSCodec: function() {
        var _super = new org.owasp.esapi.codecs.Codec();

        return {
            encode: _super.encode,

            decode: _super.decode,

            encodeCharacter: function(aImmune, c) {
                if (aImmune.contains(c)) {
                    return c;
                }

                var hex = org.owasp.esapi.codecs.getHexForNonAlphanumeric(c);
                if (hex == null) {
                    return c;
                }

                return "\\" + hex + " ";
            },

            decodeCharacter: function(oPushbackString) {
                oPushbackString.mark();
                var first = oPushbackString.next();
                if (first == null) {
                    oPushbackString.reset();
                    return null;
                }

                if (first != '\\') {
                    oPushbackString.reset();
                    return null;
                }

                var second = oPushbackString.next();
                if (second == null) {
                    oPushbackString.reset();
                    return null;
                }

                if (oPushbackString.isHexDigit(second)) {
                    var out = second;
                    for (var i = 0; i < 6; i ++) {
                        var c = oPushbackString.next();
                        if (c == null || c.charCodeAt(0) == 0x20) {
                            break;
                        }
                        if (oPushbackString.isHexDigit(c)) {
                            out += c;
                        } else {
                            input.pushback(c);
                            break;
                        }
                    }

                    try {
                        var n = parseInt(out, 16);
                        return String.fromCharCode(n);
                    } catch (e) {
                        oPushbackString.reset();
                        return null;
                    }
                }

                return second;
            }
        };
    },

    UTF8: {
        encode: function(sInput) {
            var input = sInput.replace(/\r\n/g, "\n");
            var utftext = '';

            for (var n = 0; n < input.length; n ++) {
                var c = input.charCodeAt(n);

                if (c < 128) {
                    utftext += String.fromCharCode(c);
                }
                else if (( c > 127) && (c < 2048)) {
                    utftext += String.fromCharCode((c >> 6) | 192);
                    utftext += String.fromCharCode((c & 63) | 128);
                }
                else {
                    utftext += String.fromCharCode((c >> 12) | 224);
                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                    utftext += String.fromCharCode((c & 63) | 128);
                }
            }

            return utftext;
        }
        ,

        decode: function(sInput) {
            var out = '';
            var i = c = c1 = c2 = 0;

            while (i < sInput.length) {
                c = sInput.charCodeAt(i);

                if (c < 128) {
                    out += String.fromCharCode(c);
                    i ++;
                }
                else if ((c > 191) && (c < 224)) {
                    c2 = sInput.charCodeAt(i + 1);
                    out += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                    i += 2;
                }
                else {
                    c2 = utftext.charCodeAt(i + 1);
                    c3 = utftext.charCodeAt(i + 2);
                    string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                    i += 3;
                }
            }

            return out;
        }
    },

    PercentCodec: function() {
        var _super = new org.owasp.esapi.codecs.Codec();

        var ALPHA_NUMERIC_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        var RFC_NON_ALPHANUMERIC_UNRESERVED_STR = "-._~";
        var ENCODED_NON_ALPHA_NUMERIC_UNRESERVED = true;
        var UNENCODED_STR = ALPHA_NUMERIC_STR + (ENCODED_NON_ALPHA_NUMERIC_UNRESERVED ? "" : RFC_NON_ALPHANUMERIC_UNRESERVED_STR);

        var getTwoUpperBytes = function(b) {
            var out = '';
            if (b < -128 || b > 127) {
                throw new IllegalArgumentException("b is not a byte (was " + b + ")");
            }
            b &= 0xFF;
            if (b < 0x10) {
                out += '0';
            }
            return out + b.toString(16).toUpperCase();
        };

        return {
            encode: _super.encode,

            decode: _super.decode,

            encodeCharacter: function(aImmune, c) {
                if (UNENCODED_STR.indexOf(c) > -1) {
                    return c;
                }

                var bytes = org.owasp.esapi.codecs.UTF8.encode(c);
                var out = '';
                for (var b = 0; b < bytes.length; b++) {
                    out += '%' + getTwoUpperBytes(bytes.charCodeAt(b));
                }
                return out;
            },

            decodeCharacter: function(oPushbackString) {
                oPushbackString.mark();
                var first = oPushbackString.next();
                if (first == null || first != '%') {
                    oPushbackString.reset();
                    return null;
                }

                var out = '';
                for (var i = 0; i < 2; i++) {
                    var c = oPushbackString.nextHex();
                    if (c != null) {
                        out += c;
                    }
                }
                if (out.length == 2) {
                    try {
                        var n = parseInt(out, 16);
                        return String.fromCharCode(n);
                    } catch (e) {
                    }
                }
                oPushbackString.reset();
                return null;
            }
        };
    }
};

org.owasp.esapi.codecs.Codec.hex = [];
for ( var c = 0; c < 0xFF; c ++ ) {
    if ( c >= 0x30 && c <= 0x39 || c>= 0x41 && c <= 0x5A || c >= 0x61 && c <= 0x7A ) {
        org.owasp.esapi.codecs.Codec.hex[c] = null;
    } else {
        org.owasp.esapi.codecs.Codec.hex[c] = c.toString(16);
    }
};

var entityToCharacterMap = [];
entityToCharacterMap["&quot"]        = "34";      /* 34 : quotation mark */
entityToCharacterMap["&amp"]         = "38";      /* 38 : ampersand */
entityToCharacterMap["&lt"]          = "60";        /* 60 : less-than sign */
entityToCharacterMap["&gt"]          = "62";        /* 62 : greater-than sign */
entityToCharacterMap["&nbsp"]        = "160";      /* 160 : no-break space */
entityToCharacterMap["&iexcl"]       = "161";     /* 161 : inverted exclamation mark */
entityToCharacterMap["&cent"]			= "162";	/* 162  : cent sign */
entityToCharacterMap["&pound"]			= "163";	/* 163  : pound sign */
entityToCharacterMap["&curren"]			= "164";	/* 164  : currency sign */
entityToCharacterMap["&yen"]			= "165";	/* 165  : yen sign */
entityToCharacterMap["&brvbar"]			= "166";	/* 166  : broken bar */
entityToCharacterMap["&sect"]			= "167";	/* 167  : section sign */
entityToCharacterMap["&uml"]			= "168";	/* 168  : diaeresis */
entityToCharacterMap["&copy"]			= "169";	/* 169  : copyright sign */
entityToCharacterMap["&ordf"]			= "170";	/* 170  : feminine ordinal indicator */
entityToCharacterMap["&laquo"]          = "171";    /* 171 : left-pointing double angle quotation mark */
entityToCharacterMap["&not"]			= "172";	/* 172  : not sign */
entityToCharacterMap["&shy"]			= "173";	/* 173  : soft hyphen */
entityToCharacterMap["&reg"]			= "174";	/* 174  : registered sign */
entityToCharacterMap["&macr"]			= "175";	/* 175  : macron */
entityToCharacterMap["&deg"]			= "176";	/* 176  : degree sign */
entityToCharacterMap["&plusmn"]         = "177";    /* 177 : plus-minus sign */
entityToCharacterMap["&sup2"]			= "178";	/* 178  : superscript two */
entityToCharacterMap["&sup3"]			= "179";	/* 179  : superscript three */
entityToCharacterMap["&acute"]			= "180";	/* 180  : acute accent */
entityToCharacterMap["&micro"]			= "181";	/* 181  : micro sign */
entityToCharacterMap["&para"]			= "182";	/* 182  : pilcrow sign */
entityToCharacterMap["&middot"]			= "183";	/* 183  : middle dot */
entityToCharacterMap["&cedil"]			= "184";	/* 184  : cedilla */
entityToCharacterMap["&sup1"]			= "185";	/* 185  : superscript one */
entityToCharacterMap["&ordm"]			= "186";	/* 186  : masculine ordinal indicator */
entityToCharacterMap["&raquo"]          = "187";    /* 187 : right-pointing double angle quotation mark */
entityToCharacterMap["&frac14"]			= "188";	/* 188  : vulgar fraction one quarter */
entityToCharacterMap["&frac12"]			= "189";	/* 189  : vulgar fraction one half */
entityToCharacterMap["&frac34"]			= "190";	/* 190  : vulgar fraction three quarters */
entityToCharacterMap["&iquest"]			= "191";	/* 191  : inverted question mark */
entityToCharacterMap["&Agrave"]			= "192";	/* 192  : Latin capital letter a with grave */
entityToCharacterMap["&Aacute"]			= "193";	/* 193  : Latin capital letter a with acute */
entityToCharacterMap["&Acirc"]			= "194";	/* 194  : Latin capital letter a with circumflex */
entityToCharacterMap["&Atilde"]			= "195";	/* 195  : Latin capital letter a with tilde */
entityToCharacterMap["&Auml"]			= "196";	/* 196  : Latin capital letter a with diaeresis */
entityToCharacterMap["&Aring"]			= "197";	/* 197  : Latin capital letter a with ring above */
entityToCharacterMap["&AElig"]			= "198";	/* 198  : Latin capital letter ae */
entityToCharacterMap["&Ccedil"]			= "199";	/* 199  : Latin capital letter c with cedilla */
entityToCharacterMap["&Egrave"]			= "200";	/* 200  : Latin capital letter e with grave */
entityToCharacterMap["&Eacute"]			= "201";	/* 201  : Latin capital letter e with acute */
entityToCharacterMap["&Ecirc"]			= "202";	/* 202  : Latin capital letter e with circumflex */
entityToCharacterMap["&Euml"]			= "203";	/* 203  : Latin capital letter e with diaeresis */
entityToCharacterMap["&Igrave"]			= "204";	/* 204  : Latin capital letter i with grave */
entityToCharacterMap["&Iacute"]			= "205";	/* 205  : Latin capital letter i with acute */
entityToCharacterMap["&Icirc"]			= "206";	/* 206  : Latin capital letter i with circumflex */
entityToCharacterMap["&Iuml"]			= "207";	/* 207  : Latin capital letter i with diaeresis */
entityToCharacterMap["&ETH"]			    = "208";	/* 208  : Latin capital letter eth */
entityToCharacterMap["&Ntilde"]			= "209";	/* 209  : Latin capital letter n with tilde */
entityToCharacterMap["&Ograve"]			= "210";	/* 210  : Latin capital letter o with grave */
entityToCharacterMap["&Oacute"]			= "211";	/* 211  : Latin capital letter o with acute */
entityToCharacterMap["&Ocirc"]           = "212";  /* 212 : Latin capital letter o with circumflex */
entityToCharacterMap["&Otilde"]			= "213";	 /* 213 : Latin capital letter o with tilde */
entityToCharacterMap["&Ouml"]			= "214";	 /* 214 : Latin capital letter o with diaeresis */
entityToCharacterMap["&times"]			= "215";	 /* 215 : multiplication sign */
entityToCharacterMap["&Oslash"]			= "216";	 /* 216 : Latin capital letter o with stroke */
entityToCharacterMap["&Ugrave"]			= "217";	 /* 217 : Latin capital letter u with grave */
entityToCharacterMap["&Uacute"]			= "218";	 /* 218 : Latin capital letter u with acute */
entityToCharacterMap["&Ucirc"]			= "219";	 /* 219 : Latin capital letter u with circumflex */
entityToCharacterMap["&Uuml"]			= "220";	 /* 220 : Latin capital letter u with diaeresis */
entityToCharacterMap["&Yacute"]			= "221";	 /* 221 : Latin capital letter y with acute */
entityToCharacterMap["&THORN"]			= "222";	 /* 222 : Latin capital letter thorn */
entityToCharacterMap["&szlig"]           = "223";   /* 223 : Latin small letter sharp s, German Eszett */
entityToCharacterMap["&agrave"]			= "224";	 /* 224 : Latin small letter a with grave */
entityToCharacterMap["&aacute"]			= "225";	 /* 225 : Latin small letter a with acute */
entityToCharacterMap["&acirc"]			= "226";	 /* 226 : Latin small letter a with circumflex */
entityToCharacterMap["&atilde"]			= "227";	 /* 227 : Latin small letter a with tilde */
entityToCharacterMap["&auml"]			= "228";	 /* 228 : Latin small letter a with diaeresis */
entityToCharacterMap["&aring"]			= "229";	 /* 229 : Latin small letter a with ring above */
entityToCharacterMap["&aelig"]			= "230";	 /* 230 : Latin lowercase ligature ae */
entityToCharacterMap["&ccedil"]			= "231";	 /* 231 : Latin small letter c with cedilla */
entityToCharacterMap["&egrave"]			= "232";	 /* 232 : Latin small letter e with grave */
entityToCharacterMap["&eacute"]			= "233";	 /* 233 : Latin small letter e with acute */
entityToCharacterMap["&ecirc"]			= "234";	 /* 234 : Latin small letter e with circumflex */
entityToCharacterMap["&euml"]			= "235";	 /* 235 : Latin small letter e with diaeresis */
entityToCharacterMap["&igrave"]			= "236";	 /* 236 : Latin small letter i with grave */
entityToCharacterMap["&iacute"]			= "237";	 /* 237 : Latin small letter i with acute */
entityToCharacterMap["&icirc"]			= "238";	 /* 238 : Latin small letter i with circumflex */
entityToCharacterMap["&iuml"]			= "239";	 /* 239 : Latin small letter i with diaeresis */
entityToCharacterMap["&eth"]			    = "240";	 /* 240 : Latin small letter eth */
entityToCharacterMap["&ntilde"]			= "241";	 /* 241 : Latin small letter n with tilde */
entityToCharacterMap["&ograve"]			= "242";	 /* 242 : Latin small letter o with grave */
entityToCharacterMap["&oacute"]			= "243";	 /* 243 : Latin small letter o with acute */
entityToCharacterMap["&ocirc"]			= "244";	 /* 244 : Latin small letter o with circumflex */
entityToCharacterMap["&otilde"]			= "245";	 /* 245 : Latin small letter o with tilde */
entityToCharacterMap["&ouml"]			= "246";	 /* 246 : Latin small letter o with diaeresis */
entityToCharacterMap["&divide"]			= "247";	 /* 247 : division sign */
entityToCharacterMap["&oslash"]			= "248";	 /* 248 : Latin small letter o with stroke */
entityToCharacterMap["&ugrave"]			= "249";	 /* 249 : Latin small letter u with grave */
entityToCharacterMap["&uacute"]			= "250";	 /* 250 : Latin small letter u with acute */
entityToCharacterMap["&ucirc"]			= "251";	 /* 251 : Latin small letter u with circumflex */
entityToCharacterMap["&uuml"]			= "252";	 /* 252 : Latin small letter u with diaeresis */
entityToCharacterMap["&yacute"]			= "253";	 /* 253 : Latin small letter y with acute */
entityToCharacterMap["&thorn"]			= "254";	 /* 254 : Latin small letter thorn */
entityToCharacterMap["&yuml"]			= "255";	 /* 255 : Latin small letter y with diaeresis */
entityToCharacterMap["&OElig"]			= "338";	 /* 338 : Latin capital ligature oe */
entityToCharacterMap["&oelig"]			= "339";	 /* 339 : Latin small ligature oe */
entityToCharacterMap["&Scaron"]			= "352";	 /* 352 : Latin capital letter s with caron */
entityToCharacterMap["&scaron"]			= "353";	 /* 353 : Latin small letter s with caron */
entityToCharacterMap["&Yuml"]			= "376";	 /* 376 : Latin capital letter y with diaeresis */
entityToCharacterMap["&fnof"]			= "402";	 /* 402 : Latin small letter f with hook */
entityToCharacterMap["&circ"]			= "710";	 /* 710 : modifier letter circumflex accent */
entityToCharacterMap["&tilde"]			= "732";	 /* 732 : small tilde */
entityToCharacterMap["&Alpha"]			= "913";	 /* 913 : Greek capital letter alpha */
entityToCharacterMap["&Beta"]			= "914";	 /* 914 : Greek capital letter beta */
entityToCharacterMap["&Gamma"]			= "915";	 /* 915 : Greek capital letter gamma */
entityToCharacterMap["&Delta"]			= "916";	 /* 916 : Greek capital letter delta */
entityToCharacterMap["&Epsilon"]			= "917";	 /* 917 : Greek capital letter epsilon */
entityToCharacterMap["&Zeta"]			= "918";	 /* 918 : Greek capital letter zeta */
entityToCharacterMap["&Eta"]			    = "919";	 /* 919 : Greek capital letter eta */
entityToCharacterMap["&Theta"]			= "920";	 /* 920 : Greek capital letter theta */
entityToCharacterMap["&Iota"]			= "921";	 /* 921 : Greek capital letter iota */
entityToCharacterMap["&Kappa"]			= "922";	 /* 922 : Greek capital letter kappa */
entityToCharacterMap["&Lambda"]			= "923";	 /* 923 : Greek capital letter lambda */
entityToCharacterMap["&Mu"]			= "924";	 /* 924 : Greek capital letter mu */
entityToCharacterMap["&Nu"]			= "925";	 /* 925 : Greek capital letter nu */
entityToCharacterMap["&Xi"]			= "926";	 /* 926 : Greek capital letter xi */
entityToCharacterMap["&Omicron"]			= "927";	 /* 927 : Greek capital letter omicron */
entityToCharacterMap["&Pi"]			= "928";	 /* 928 : Greek capital letter pi */
entityToCharacterMap["&Rho"]			= "929";	 /* 929 : Greek capital letter rho */
entityToCharacterMap["&Sigma"]			= "931";	 /* 931 : Greek capital letter sigma */
entityToCharacterMap["&Tau"]			= "932";	 /* 932 : Greek capital letter tau */
entityToCharacterMap["&Upsilon"]			= "933";	 /* 933 : Greek capital letter upsilon */
entityToCharacterMap["&Phi"]			= "934";	 /* 934 : Greek capital letter phi */
entityToCharacterMap["&Chi"]			= "935";	 /* 935 : Greek capital letter chi */
entityToCharacterMap["&Psi"]			= "936";	 /* 936 : Greek capital letter psi */
entityToCharacterMap["&Omega"]			= "937";	 /* 937 : Greek capital letter omega */
entityToCharacterMap["&alpha"]			= "945";	 /* 945 : Greek small letter alpha */
entityToCharacterMap["&beta"]			= "946";	 /* 946 : Greek small letter beta */
entityToCharacterMap["&gamma"]			= "947";	 /* 947 : Greek small letter gamma */
entityToCharacterMap["&delta"]			= "948";	 /* 948 : Greek small letter delta */
entityToCharacterMap["&epsilon"]			= "949";	 /* 949 : Greek small letter epsilon */
entityToCharacterMap["&zeta"]			= "950";	 /* 950 : Greek small letter zeta */
entityToCharacterMap["&eta"]			= "951";	 /* 951 : Greek small letter eta */
entityToCharacterMap["&theta"]			= "952";	 /* 952 : Greek small letter theta */
entityToCharacterMap["&iota"]			= "953";	 /* 953 : Greek small letter iota */
entityToCharacterMap["&kappa"]			= "954";	 /* 954 : Greek small letter kappa */
entityToCharacterMap["&lambda"]			= "955";	 /* 955 : Greek small letter lambda */
entityToCharacterMap["&mu"]			= "956";	 /* 956 : Greek small letter mu */
entityToCharacterMap["&nu"]			= "957";	 /* 957 : Greek small letter nu */
entityToCharacterMap["&xi"]			= "958";	 /* 958 : Greek small letter xi */
entityToCharacterMap["&omicron"]			= "959";	 /* 959 : Greek small letter omicron */
entityToCharacterMap["&pi"]			= "960";	 /* 960 : Greek small letter pi */
entityToCharacterMap["&rho"]			= "961";	 /* 961 : Greek small letter rho */
entityToCharacterMap["&sigmaf"]			= "962";	 /* 962 : Greek small letter final sigma */
entityToCharacterMap["&sigma"]			= "963";	 /* 963 : Greek small letter sigma */
entityToCharacterMap["&tau"]			= "964";	 /* 964 : Greek small letter tau */
entityToCharacterMap["&upsilon"]			= "965";	 /* 965 : Greek small letter upsilon */
entityToCharacterMap["&phi"]			= "966";	 /* 966 : Greek small letter phi */
entityToCharacterMap["&chi"]			= "967";	 /* 967 : Greek small letter chi */
entityToCharacterMap["&psi"]			= "968";	 /* 968 : Greek small letter psi */
entityToCharacterMap["&omega"]			= "969";	 /* 969 : Greek small letter omega */
entityToCharacterMap["&thetasym"]			= "977";	 /* 977 : Greek theta symbol */
entityToCharacterMap["&upsih"]			= "978";	 /* 978 : Greek upsilon with hook symbol */
entityToCharacterMap["&piv"]			= "982";	 /* 982 : Greek pi symbol */
entityToCharacterMap["&ensp"]			= "8194";	 /* 8194 : en space */
entityToCharacterMap["&emsp"]			= "8195";	 /* 8195 : em space */
entityToCharacterMap["&thinsp"]			= "8201";	 /* 8201 : thin space */
entityToCharacterMap["&zwnj"]            = "8204"; /* 8204 : zero width non-joiner */
entityToCharacterMap["&zwj"]			= "8205";	 /* 8205 : zero width joiner */
entityToCharacterMap["&lrm"]             = "8206"; /* 8206 : left-to-right mark */
entityToCharacterMap["&rlm"]             = "8207"; /* 8207 : right-to-left mark */
entityToCharacterMap["&ndash"]			= "8211";	 /* 8211 : en dash */
entityToCharacterMap["&mdash"]			= "8212";	 /* 8212 : em dash */
entityToCharacterMap["&lsquo"]			= "8216";	 /* 8216 : left single quotation mark */
entityToCharacterMap["&rsquo"]			= "8217";	 /* 8217 : right single quotation mark */
entityToCharacterMap["&sbquo"]           = "8218";  /* 8218 : single low-9 quotation mark */
entityToCharacterMap["&ldquo"]			= "8220";	 /* 8220 : left double quotation mark */
entityToCharacterMap["&rdquo"]			= "8221";	 /* 8221 : right double quotation mark */
entityToCharacterMap["&bdquo"]           = "8222";  /* 8222 : double low-9 quotation mark */
entityToCharacterMap["&dagger"]			= "8224";	 /* 8224 : dagger */
entityToCharacterMap["&Dagger"]			= "8225";	 /* 8225 : double dagger */
entityToCharacterMap["&bull"]			= "8226";	 /* 8226 : bullet */
entityToCharacterMap["&hellip"]			= "8230";	 /* 8230 : horizontal ellipsis */
entityToCharacterMap["&permil"]			= "8240";	 /* 8240 : per mille sign */
entityToCharacterMap["&prime"]			= "8242";	 /* 8242 : prime */
entityToCharacterMap["&Prime"]			= "8243";	 /* 8243 : double prime */
entityToCharacterMap["&lsaquo"]          = "8249";  /* 8249 : single left-pointing angle quotation mark */
entityToCharacterMap["&rsaquo"]          = "8250";  /* 8250 : single right-pointing angle quotation mark */
entityToCharacterMap["&oline"]			= "8254";	 /* 8254 : overline */
entityToCharacterMap["&frasl"]			= "8260";	 /* 8260 : fraction slash */
entityToCharacterMap["&euro"]			= "8364";	 /* 8364 : euro sign */
entityToCharacterMap["&image"]           = "8365";  /* 8465 : black-letter capital i */
entityToCharacterMap["&weierp"]          = "8472";  /* 8472 : script capital p, Weierstrass p */
entityToCharacterMap["&real"]            = "8476";  /* 8476 : black-letter capital r */
entityToCharacterMap["&trade"]			= "8482";	 /* 8482 : trademark sign */
entityToCharacterMap["&alefsym"]			= "8501";	 /* 8501 : alef symbol */
entityToCharacterMap["&larr"]			= "8592";	 /* 8592 : leftwards arrow */
entityToCharacterMap["&uarr"]			= "8593";	 /* 8593 : upwards arrow */
entityToCharacterMap["&rarr"]			= "8594";	 /* 8594 : rightwards arrow */
entityToCharacterMap["&darr"]			= "8595";	 /* 8595 : downwards arrow */
entityToCharacterMap["&harr"]			= "8596";	 /* 8596 : left right arrow */
entityToCharacterMap["&crarr"]			= "8629";	 /* 8629 : downwards arrow with corner leftwards */
entityToCharacterMap["&lArr"]			= "8656";	 /* 8656 : leftwards double arrow */
entityToCharacterMap["&uArr"]			= "8657";	 /* 8657 : upwards double arrow */
entityToCharacterMap["&rArr"]			= "8658";	 /* 8658 : rightwards double arrow */
entityToCharacterMap["&dArr"]			= "8659";	 /* 8659 : downwards double arrow */
entityToCharacterMap["&hArr"]			= "8660";	 /* 8660 : left right double arrow */
entityToCharacterMap["&forall"]			= "8704";	 /* 8704 : for all */
entityToCharacterMap["&part"]			= "8706";	 /* 8706 : partial differential */
entityToCharacterMap["&exist"]			= "8707";	 /* 8707 : there exists */
entityToCharacterMap["&empty"]			= "8709";	 /* 8709 : empty set */
entityToCharacterMap["&nabla"]			= "8711";	 /* 8711 : nabla */
entityToCharacterMap["&isin"]			= "8712";	 /* 8712 : element of */
entityToCharacterMap["&notin"]			= "8713";	 /* 8713 : not an element of */
entityToCharacterMap["&ni"]			    = "8715";	 /* 8715 : contains as member */
entityToCharacterMap["&prod"]            = "8719";  /* 8719 : n-ary product */
entityToCharacterMap["&sum"]             = "8721";  /* 8721 : n-ary summation */
entityToCharacterMap["&minus"]			= "8722";	 /* 8722 : minus sign */
entityToCharacterMap["&lowast"]			= "8727";	 /* 8727 : asterisk operator */
entityToCharacterMap["&radic"]			= "8730";	 /* 8730 : square root */
entityToCharacterMap["&prop"]			= "8733";	 /* 8733 : proportional to */
entityToCharacterMap["&infin"]			= "8734";	 /* 8734 : infinity */
entityToCharacterMap["&ang"]			= "8736";	 /* 8736 : angle */
entityToCharacterMap["&and"]			= "8743";	 /* 8743 : logical and */
entityToCharacterMap["&or"]			= "8744";	 /* 8744 : logical or */
entityToCharacterMap["&cap"]			= "8745";	 /* 8745 : intersection */
entityToCharacterMap["&cup"]			= "8746";	 /* 8746 : union */
entityToCharacterMap["&int"]			= "8747";	 /* 8747 : integral */
entityToCharacterMap["&there4"]			= "8756";	 /* 8756 : therefore */
entityToCharacterMap["&sim"]			= "8764";	 /* 8764 : tilde operator */
entityToCharacterMap["&cong"]			= "8773";	 /* 8773 : congruent to */
entityToCharacterMap["&asymp"]			= "8776";	 /* 8776 : almost equal to */
entityToCharacterMap["&ne"]			= "8800";	 /* 8800 : not equal to */
entityToCharacterMap["&equiv"]           = "8801";   /* 8801 : identical to, equivalent to */
entityToCharacterMap["&le"]              = "8804"; /* 8804 : less-than or equal to */
entityToCharacterMap["&ge"]              = "8805"; /* 8805 : greater-than or equal to */
entityToCharacterMap["&sub"]			= "8834";	 /* 8834 : subset of */
entityToCharacterMap["&sup"]			= "8835";	 /* 8835 : superset of */
entityToCharacterMap["&nsub"]			= "8836";	 /* 8836 : not a subset of */
entityToCharacterMap["&sube"]			= "8838";	 /* 8838 : subset of or equal to */
entityToCharacterMap["&supe"]			= "8839";	 /* 8839 : superset of or equal to */
entityToCharacterMap["&oplus"]			= "8853";	 /* 8853 : circled plus */
entityToCharacterMap["&otimes"]			= "8855";	 /* 8855 : circled times */
entityToCharacterMap["&perp"]			= "8869";	 /* 8869 : up tack */
entityToCharacterMap["&sdot"]			= "8901";	 /* 8901 : dot operator */
entityToCharacterMap["&lceil"]			= "8968";	 /* 8968 : left ceiling */
entityToCharacterMap["&rceil"]			= "8969";	 /* 8969 : right ceiling */
entityToCharacterMap["&lfloor"]			= "8970";	 /* 8970 : left floor */
entityToCharacterMap["&rfloor"]			= "8971";	 /* 8971 : right floor */
entityToCharacterMap["&lang"]            = "9001";  /* 9001 : left-pointing angle bracket */
entityToCharacterMap["&rang"]            = "9002";  /* 9002 : right-pointing angle bracket */
entityToCharacterMap["&loz"]			= "9674";	 /* 9674 : lozenge */
entityToCharacterMap["&spades"]			= "9824";	 /* 9824 : black spade suit */
entityToCharacterMap["&clubs"]			= "9827";	 /* 9827 : black club suit */
entityToCharacterMap["&hearts"]			= "9829";	 /* 9829 : black heart suit */
entityToCharacterMap["&diams"]			= "9830";	 /* 9830 : black diamond suit */

var characterToEntityMap = [];

for ( var entity in entityToCharacterMap ) {
    characterToEntityMap[entityToCharacterMap[entity]] = entity;
}

/*
 * OWASP Enterprise Security API (ESAPI)
 *
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * <a href="http://www.owasp.org/index.php/ESAPI">http://www.owasp.org/index.php/ESAPI</a>.
 *
 * Copyright (c) 2008 - The OWASP Foundation
 *
 * The ESAPI is published by OWASP under the BSD license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 */

$namespace('org.owasp.esapi.reference.encoding');
$require('org.owasp.esapi.codecs');

org.owasp.esapi.reference.encoding = {
    DefaultEncoder: function(aCodecs) {
        var _codecs = [],
                _htmlCodec = new org.owasp.esapi.codecs.HTMLEntityCodec(),
                _javascriptCodec = new org.owasp.esapi.codecs.JavascriptCodec(),
                _cssCodec = new org.owasp.esapi.codecs.CSSCodec(),
                _percentCodec = new org.owasp.esapi.codecs.PercentCodec();

        if (!aCodecs) {
            _codecs.push(_htmlCodec);
            _codecs.push(_javascriptCodec);
            _codecs.push(_cssCodec);
            _codecs.push(_percentCodec);
        } else {
            _codecs = aCodecs;
        }

        var IMMUNE_HTML = new Array(',', '.', '-', '_', ' ');
        var IMMUNE_HTMLATTR = new Array(',', '.', '-', '_');
        var IMMUNE_CSS = new Array();
        var IMMUNE_JAVASCRIPT = new Array(',', '.', '_');

        return {
            cananicalize: function(sInput, bStrict) {
                if (!sInput) {
                    return null;
                }
                var working = sInput, codecFound = null, mixedCount = 1, foundCount = 0, clean = false;
                while (!clean) {
                    clean = true;

                    _codecs.each(function(codec) {
                        var old = working;
                        working = codec.decode(working);

                        if (old != working) {
                            if (codecFound != null && codecFound != codec) {
                                mixedCount ++;
                            }
                            codecFound = codec;
                            if (clean) {
                                foundCount ++;
                            }
                            clean = false;
                        }
                    });
                }

                if (foundCount >= 2 && mixedCount > 1) {
                    if (bStrict) {
                        throw new org.owasp.esapi.IntrusionException("Input validation failure", "Multiple (" + foundCount + "x) and mixed encoding (" + mixedCount + "x) detected in " + sInput);
                    }
                }
                else if (foundCount >= 2) {
                    if (bStrict) {
                        throw new org.owasp.esapi.IntrusionException("Input validation failure", "Multiple (" + foundCount + "x) encoding detected in " + sInput);
                    }
                }
                else if (mixedCount > 1) {
                        if (bStrict) {
                            throw new org.owasp.esapi.IntrusionException("Input validation failure", "Mixed (" + mixedCount + "x) encoding detected in " + sInput);
                        }
                    }
                return working;
            },

            normalize: function(sInput) {
                return sInput.replace(/[^\x00-\x7F]/g, '');
            },

            encodeForHTML: function(sInput) {
                return !sInput ? null : _htmlCodec.encode(IMMUNE_HTML, sInput);
            },

            decodeForHTML: function(sInput) {
                return !sInput ? null : _htmlCodec.decode(sInput);
            },

            encodeForHTMLAttribute: function(sInput) {
                return !sInput ? null : _htmlCodec.encode(IMMUNE_HTMLATTR, sInput);
            },

            encodeForCSS: function(sInput) {
                return !sInput ? null : _cssCodec.encode(IMMUNE_CSS, sInput);
            },

            encodeForJavaScript: function(sInput) {
                return !sInput ? null : _javascriptCodec.encode(IMMUNE_JAVASCRIPT, sInput);
            },

            encodeForJavascript: this.encodeForJavaScript,

            encodeForURL: function(sInput) {
                return !sInput ? null : escape(sInput);
            },

            decodeFromURL: function(sInput) {
                return !sInput ? null : unescape(sInput);
            },

            encodeForBase64: function(sInput) {
                return !sInput ? null : org.owasp.esapi.codecs.Base64.encode(sInput);
            },

            decodeFromBase64: function(sInput) {
                return !sInput ? null : org.owasp.esapi.codecs.Base64.decode(sInput);
            }
        };
    }
};/*
 * OWASP Enterprise Security API (ESAPI)
 *
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * <a href="http://www.owasp.org/index.php/ESAPI">http://www.owasp.org/index.php/ESAPI</a>.
 *
 * Copyright (c) 2008 - The OWASP Foundation
 *
 * The ESAPI is published by OWASP under the BSD license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 */

$namespace('org.owasp.esapi.reference.logging');

org.owasp.esapi.reference.logging = {
    Log4JSLogFactory: function() {
        var loggersMap = Array();

        var Log4JSLogger = function( sModuleName ) {
            var jsLogger = null;
            var moduleName = null;
            var Level = Log4js.Level;

            jsLogger = Log4js.getLogger( moduleName );

            var convertESAPILevel = function( nLevel ) {
                var Logger = org.owasp.esapi.Logger;
                switch (nLevel) {
                    case Logger.OFF:        return Log4js.Level.OFF;
                    case Logger.FATAL:      return Log4js.Level.FATAL;
                    case Logger.ERROR:      return Log4js.Level.ERROR;
                    case Logger.WARNING:    return Log4js.Level.WARN;
                    case Logger.INFO:       return Log4js.Level.INFO;
                    case Logger.DEBUG:      return Log4js.Level.DEBUG;
                    case Logger.TRACE:      return Log4js.Level.TRACE;
                    case Logger.ALL:        return Log4js.Level.ALL;
                }
            };

            return {
                setLevel: function( nLevel ) {
                    try {
                        jsLogger.setLevel( convertESAPILevel( nLevel ) );
                    } catch (e) {
                        this.error( org.owasp.esapi.Logger.SECURITY_FAILURE, "", e );
                    }
                },

                trace: function( oEventType, sMessage, oException ) {
                    this.log( Level.TRACE, oEventType, sMessage, oException );
                },

                debug: function( oEventType, sMessage, oException ) {
                    this.log( Level.DEBUG, oEventType, sMessage, oException );
                },

                info: function( oEventType, sMessage, oException ) {
                    this.log( Level.INFO, oEventType, sMessage, oException );
                },

                warning: function( oEventType, sMessage, oException ) {
                    this.log( Level.WARN, oEventType, sMessage, oException );
                },

                error: function( oEventType, sMessage, oException ) {
                    this.log( Level.ERROR, oEventType, sMessage, oException );
                },

                fatal: function( oEventType, sMessage, oException ) {
                    this.log( Level.FATAL, oEventType, sMessage, oException );
                },

                log: function( oLevel, oEventType, sMessage, oException ) {
                    switch(oLevel) {
                        case Level.TRACE:       if ( !jsLogger.isTraceEnabled() ) { return; } break;
                        case Level.DEBUG:       if ( !jsLogger.isDebugEnabled() ) { return; } break;
                        case Level.INFO:        if ( !jsLogger.isInfoEnabled()  ) { return; } break;
                        case Level.WARNING:     if ( !jsLogger.isWarnEnabled()  ) { return; } break;
                        case Level.ERROR:       if ( !jsLogger.isErrorEnabled() ) { return; } break;
                        case Level.FATAL:       if ( !jsLogger.isFatalEnabled() ) { return; } break;
                    }

                    if ( !sMessage ) {
                        sMessage = "";
                    }

                    var clean = sMessage.replace("\n","_").replace("\r","_");
                    if ( $ESAPI.properties.logging.EncodingRequired ) {
                        clean = $ESAPI.encoder().encodeForHTML(clean);
                        if ( clean != sMessage) {
                            clean += " [Encoded]";
                        }
                    }

                    var appInfo =   ( $ESAPI.properties.logging.LogUrl ? window.location.href : "" ) +
                                    ( $ESAPI.properties.logging.LogApplicationName ? "/" + $ESAPI.properties.logging.ApplicationName : "" );

                    jsLogger.log( oLevel, appInfo != "" ? "[" + appInfo + "] " : "" + clean, oException );
                },

                addAppender: function( oAppender ) {
                    jsLogger.addAppender( oAppender );
                },

                isDebugEnabled: function()   { return jsLogger.isDebugEnabled(); },
                isErrorEnabled: function()   { return jsLogger.isErrorEnabled(); },
                isFatalEnabled: function()   { return jsLogger.isFatalEnabled(); },
                isInfoEnabled: function()    { return jsLogger.isInfoEnabled(); },
                isTraceEnabled: function()   { return jsLogger.isTraceEnabled(); },
                isWarningEnabled: function() { return jsLogger.isWarnEnabled(); }
            };
        };

        return {
            getLogger: function ( moduleName ) {
                var key = ( typeof moduleName == 'string' ) ? moduleName : moduleName.constructor.toString();
                var logger = loggersMap[key];
                if ( !logger ) {
                    logger = new Log4JSLogger(key);

                    if ( Log4js.config && Log4js.config[moduleName] ) {
                        logger.setLevel( Log4js.config[moduleName].level?Log4js.config[moduleName].level:eval($ESAPI.properties.logging.Level));
                        if ( Log4js.config[moduleName].appenders ) {
                            Log4js.config[moduleName].appenders.each(function(e){
                                logger.addAppender(e);
                            });
                        }
                    } else {
                        eval('logger.setLevel( '+$ESAPI.properties.logging.Level+' );');
                        $ESAPI.properties.logging.Appenders.each(function(e){
                            logger.addAppender(e);
                        });
                    }

                    loggersMap[key] = logger;
                }
                return logger;
            }
        };
    }
};