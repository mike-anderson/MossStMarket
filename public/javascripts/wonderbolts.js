	$(document).ready(function () {
	
		$('#overview td').on("click",
			function() {
				$(this).children('.tooltip').addClass('clicked');
			}
		);

		$(".close").on("click", function (e) {
			e.stopPropagation();
			$(this).parent(".tooltip").removeClass("clicked");
		});
		
		//When you click on a link with class of poplight and the href starts with a # 
		$('a.poplight[href^=#]').click(function() {
			var popID = $(this).attr('rel'); //Get Popup Name
			var popURL = $(this).attr('href'); //Get Popup href to define size

			//Pull Query & Variables from href URL
			var query= popURL.split('?');
			var dim= query[1].split('&');
			var popWidth = dim[0].split('=')[1]; //Gets the first query string value

			//Fade in the Popup and add close button
			$('#' + popID).fadeIn().css({ 'width': Number( popWidth ) }).prepend('<a href="#" class="close"><img src="/public/images/close.png" class="btn_close" title="Close Window" alt="Close" /></a>');

			//Define margin for center alignment (vertical   horizontal) - we add 80px to the height/width to accomodate for the padding  and border width defined in the css
			var popMargTop = ($('#' + popID).height() + 80) / 2;
			var popMargLeft = ($('#' + popID).width() + 80) / 2;

			//Apply Margin to Popup
			$('#' + popID).css({
				'margin-top' : -popMargTop,
				'margin-left' : -popMargLeft
			});

			//Fade in Background
			$('body').append('<div id="fade"></div>'); //Add the fade layer to bottom of the body tag.
			$('#fade').css({'filter' : 'alpha(opacity=80)'}).fadeIn(); //Fade in the fade layer - .css({'filter' : 'alpha(opacity=80)'}) is used to fix the IE Bug on fading transparencies 

			return false;
		});

		
		if ($('#immediate').length > 0) {
			var popWidth = 500;
			//Fade in the Popup and add close button
			$('#immediate').css({ 'width': Number( popWidth ) }).prepend('<a href="#" class="close"><img src="/public/images/close.png" class="btn_close" title="Close Window" alt="Close" /></a>');

			//Define margin for center alignment (vertical   horizontal) - we add 80px to the height/width to accomodate for the padding  and border width defined in the css
			var popMargTop = ($('#immediate').height() + 80) / 2;
			var popMargLeft = ($('#immediate').width() + 80) / 2;

			//Apply Margin to Popup
			$('#immediate').css({
				'display': 'block',
				'margin-top' : -popMargTop,
				'margin-left' : -popMargLeft
			});

			//Fade in Background
			$('body').append('<div id="fade"></div>'); //Add the fade layer to bottom of the body tag.
			$('#fade').css({'filter' : 'alpha(opacity=80)'}).fadeIn(); //Fade in the fade layer - .css({'filter' : 'alpha(opacity=80)'}) is used to fix the IE Bug on fading transparencies 
		}
		
		//Close Popups and Fade Layer
		$('a.close, #fade').live('click', function() { //When clicking on the close or fade layer...
			$('#fade , .popup').fadeOut(function() {
				$('#fade, a.close').remove();  //fade them both out
			});
			return false;
		});
		$('.merchant').addClass('hidden');
		$('a.merchantlink[href^=#]').click(function() {
			
			var ID = $(this).attr('rel'); //Get merchant ID
			
			$('.merchant').addClass('hidden');
			$('#' + ID).removeClass('hidden');			
		});
		$(".dateDropdown").change(function(e){
			window.location = $(this).val();
		});
		$(".categoryDropDown").change(function(e){
			window.location = $(this).val();
		});
	});