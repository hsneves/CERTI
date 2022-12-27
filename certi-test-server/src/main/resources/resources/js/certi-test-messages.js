'use strict';

if (typeof module !== 'undefined') {
	module.exports = CertiTestMessages;
}

const MessageType = {
	WARNING: 'WARNING',
	INFO: 'INFO',
	SUCCESS: 'SUCCESS',
	ERROR: 'ERROR'
}

function CertiTestMessages(debug, component, type, message) {
	this.type = type;
	this.component = component;
	this.message = message;
	this.debug = debug;
}

CertiTestMessages.prototype = {

	/**
	 * Apresenta a mensagem na UI do usuário de forma persistente
	 */
	showPersistentMessage: function() {
	
		if (this.debug) {
			console.log('CertiTestMessages | showPersistentMessage() - type = [' + this.type + '], msg = [' + this.message + '], component = [' + this.component + ']');
		}
		
		let msgContainer = this.component;
		
		if (msgContainer != null) {
		
			this._setMessage(msgContainer);
			
		} else {
		
			if (this.debug) {
				console.log('CertiTestMessages | showPersistentMessage() - container da mensagem não encontrado');
			}		
		}
	},

	/**
	 * Apresenta a mensagem na UI do usuário removendo após um certo tempo
	 */
	showMessage: function() {
	
		if (this.debug) {
			console.log('CertiTestMessages | showMessage() - type = [' + this.type + '], msg = [' + this.message + '], component = [' + this.component + ']');
		}
	
		let msgContainer = this.component;
		let dismissTime = 4000;
		
		if (msgContainer != null) {
		
			this._setMessage(msgContainer);
        	
            msgContainer.delay(dismissTime).fadeTo(500, 0, 
                function() { 
                    msgContainer.hide();
            	});
            	
		} else {
		
			if (this.debug) {
				console.log('CertiTestMessages | _setMessage() - container da mensagem não encontrado');
			}
		}    	
	},
	
	/**
	 *	Atribui a mensagem ao container
	 */
	_setMessage: function(msgContainer) {

		if (this.debug) {
			console.log('CertiTestMessages | _setMessage() - type = [' + this.type + '], msg = [' + this.message + '], component = [' + this.component + ']');
		}					
	
		msgContainer.removeClass (function (index, className) {
            return (className.match (/(^|\s)alert-\S+/g) || []).join(' ');
        });        

		msgContainer.empty();
        msgContainer.text(this.message);
        msgContainer.html(msgContainer.html().replace(/\n/g,'<br/>'));
	
		switch (this.type) {
		    case MessageType.ERROR:
		    	msgContainer.addClass('alert-danger');
		        break;
		    case MessageType.INFO:
		        msgContainer.addClass('alert-primary');
		        break;
		    case MessageType.SUCCESS:
		        msgContainer.addClass('alert-success');
		        break;                
		    case MessageType.WARNING:
		        msgContainer.addClass('alert-warning');
		        break; 
		}
		
		msgContainer.stop();
    	msgContainer.css('opacity', 1.0);
    	msgContainer.show();		
		
	}

}