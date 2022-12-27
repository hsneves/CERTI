'use strict';

if (typeof module !== 'undefined') {
	module.exports = CertiTestUrlBuilder;
}

function CertiTestUrlBuilder(debug, contextPath, restPath) {
	this.contextPath = contextPath;
	this.restPath = restPath;
	this.debug = debug;
}

CertiTestUrlBuilder.prototype = {

	/**
	 * Retorna com o context path 
	 */
	getUrl: function(url) {
		let result = this.contextPath + url;
		if (this.debug) {
			console.log('CertiTestUrlBuilder | getUrl() - url = [' + result + ']');
		} 
		return result;
	},
	
	/**
	 * Retorna com o context path e o prefixo dos serviços REST 
	 */
	getRestUrl: function(url) {
		let result = this.contextPath + this.restPath + url;
		if (this.debug) {
			console.log('CertiTestUrlBuilder | getUrl() - url = [' + result + ']');
		} 
		return result;	
	}

}