$(document).ready(() => {
	$('.imageThumbnail').click(function () {
		var imageUrl = $(this).find('img').attr('src');
		$('#imageViewer').attr('src', imageUrl);
	});
});