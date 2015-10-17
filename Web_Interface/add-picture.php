<?php 

    session_start();
    
    if( !isset( $_SESSION['admin'] ) || !$_SESSION['admin'] ) {
        header('Location: index.php');
    }
    
    $css_links = '<link href="./css/bootstrap.min.css" rel="stylesheet">';
    $js_links = '<script src="./js/jquery-1.10.2.min.js"></script>'
              . '<script src="./js/jquery.ui.widget.js"></script>'
              . '<script src="./js/jquery.iframe-transport.js"></script>'
              . '<script src="./js/jquery.fileupload.js"></script>'
              . '<script src="./js/bootstrap.min.js"></script>'
              . '<script src="./js/image-upload.js"></script>'
              . '<script>$(function(){ $("#navbar>ul>li:nth-child(2)").addClass("active"); })</script>';
              
    
    require './views/components/header.php';
    
    require './views/components/navigation.php';
    
    require './views/add_picture.php';
    
    require './views/components/footer.php';