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
        
        CSS_HIDDEN = 'aui-helper-hidden'
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

            				var flowNodesTooltip = new A.Tooltip({
            			         trigger: flowNodes,
            			         align: { points: [ 'tc', 'bc' ] },
            			         cssClass: 'rp-tooltip',
            			         showArrow: false,
            			         title: true
            			 	}).render();
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
