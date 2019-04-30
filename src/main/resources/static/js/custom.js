jQuery(document).ready(function() {
	
	"use strict";
	// Your custom js code goes here.
	var goUnderSlider = function() {

		$('.js-gounder').on('click', function(event){

			event.preventDefault();

			$('html, body').animate({
				scrollTop: $('#under-slider').offset().top
			}, 500, 'easeInOutExpo');

			return false;
		});

		$(window).scroll(function(){
			var $win = $(window);
			if ($win.scrollTop() > 200) {
				$('.js-under').addClass('active');
			} else {
				$('.js-under').removeClass('active');
			}

		});

	};

	$(document).ready(function(){
		goUnderSlider();
	});

});