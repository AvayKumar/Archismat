$(function() {
        
        var base_url = 'http://localhost/app/';
        
        
        $('#file-upload div.image-preview button').click(function(){
            
            $('#file-upload button.browse').css({'display':'block'});
            $('#file-upload div.image-preview').css({'display':'none'});
            
            $('form input.image-name').remove();
            
            $.post( $(this).attr('delete-url') , 
                {'delete_type':'DELETE'},
                function( result ){
                    
                });
        });
        
        
    	$('#imageUpload').fileupload({
            url:  base_url + 'uploads/',
	    dataType: 'json',
	    start: function(e, data) {
                
                $('#file-upload button.browse').css({'display':'none'});
                $('#file-upload div.progress').css({'display':'block'});
                
	    },
	    done: function (e, data) {
                
                console.log( data );
                
	        var imageURL = data.result.files[0].previewUrl;
                var imageName = data.result.files[0].name;
                var deleteUrl = data.result.files[0].deleteUrl;
                
	    	$('#file-upload div.image-preview img').attr('src', imageURL);
                
                $('#file-upload div.image-preview').css({'display':'block'});
                
                $('#file-upload div.progress').css({'display':'none'});
                
                $('#file-upload div.image-preview button').attr('delete-url', deleteUrl);
                
                $('form').prepend( '<input class="image-name" type="hidden" name="image-name" value="' + imageName + '"/>' );
	    },
	    progressall: function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#file-upload div.progress div.progress-bar').css( 'width', progress + '%' );
	    }
	});
    
});