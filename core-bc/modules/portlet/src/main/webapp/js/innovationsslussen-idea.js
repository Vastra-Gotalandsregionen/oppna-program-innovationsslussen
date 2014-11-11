/**
* Copyright 2010 Västra Götalandsregionen
*
*   This library is free software; you can redistribute it and/or modify
*   it under the terms of version 2.1 of the GNU Lesser General Public
*   License as published by the Free Software Foundation.
*
*   This library is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU Lesser General Public License for more details.
*
*   You should have received a copy of the GNU Lesser General Public
*   License along with this library; if not, write to the
*   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
*   Boston, MA 02111-1307  USA
*
*/

AUI().add('innovationsslussen-idea',function(A) {
    var Lang = A.Lang,
        isArray = Lang.isArray,
        isFunction = Lang.isFunction,
        isNull = Lang.isNull,
        isObject = Lang.isObject,
        isString = Lang.isString,
        isUndefined = Lang.isUndefined,
        getClassName = A.ClassNameManager.getClassName,
        concat = function() {
            return Array.prototype.slice.call(arguments).join(SPACE);
        },
        
        NAME = 'innovationsslussen-idea',
        NS = 'innovationsslussen-idea',
        
        COMMENTS_INPUT = 'commentsInput',
        PORTLET_NAMESPACE = 'portletNamespace',
        PORTLET_NODE = 'portletNode',
        
        CSS_HIDDEN = 'hide'
    ;
        
    var InnovationsslussenIdea = A.Component.create(
            {
                ATTRS: {
                	
                	commentsInput: {
                		setter: A.one
                	},
                	
                	portletNamespace: {
                		value: ''
                	},
                	
                	portletNode: {
                		setter: A.one
                	},
                	
                	someAttr: {
                		value: ''
                	}
                },
                EXTENDS: A.Component,
                NAME: NAME,
                NS: NS,
                
                prototype: {
                	
                	flowNodesTooltip: null,
                	
                    initializer: function(config) {
                        var instance = this;
                    },
                    
                    renderUI: function() {
                        var instance = this;
                        
                        instance._initTooltips();
                    },
    
                    bindUI: function() {
                        var instance = this;
                        
                        var portletNode = instance.get(PORTLET_NODE);
                        
                        // Bind commentLink click
                        portletNode.all('.rp-toolbar .comment a').on('click', instance._onCommentLinkClick, instance);
                    },
                    
					_focusCommentsInput: function() {
						var instance = this;
						
						var commentsInput = instance.get(COMMENTS_INPUT);
						
						if(commentsInput) {
							Liferay.Util.focusFormField(commentsInput);	
						}
					},
                    
                    _onCommentLinkClick: function(e) {
                    	var instance = this;
                    	e.halt();
                    	instance._focusCommentsInput();
                    },
                    
                    _initTooltips: function() {
                        var instance = this;
                        
                        var portletNode = instance.get(PORTLET_NODE);
                        
            			if(portletNode) {
            				var flowNodes = portletNode.all('.idea-flow-list li');

            				instance.flowNodesTooltip = new A.TooltipDelegate({
                                 html: true,
                                 opacity: 1,
            			         trigger: '.idea-flow-list li'
            			 	});
            			}
                    },
                    
                    _someFunction: function() {
                        var instance = this;
                    }

                }
            }
    );

    A.InnovationsslussenIdea = InnovationsslussenIdea;
        
    },1, {
        requires: [
	       'aui-base',
	       'aui-tooltip'
      ]
    }
);
