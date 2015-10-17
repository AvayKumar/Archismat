<?php 

    session_start();
    
    if( !isset( $_SESSION['admin'] ) || !$_SESSION['admin'] ) {
        header('Location: index.php');
    }
    
    $css_links = '<link href="./css/bootstrap.min.css" rel="stylesheet">';
    $js_links = '<script src="./js/jquery-1.10.2.min.js"></script>'
              . '<script src="./js/bootstrap.min.js"></script>'
              . '<script>$(function(){ $("#navbar>ul>li:nth-child(3)").addClass("active"); })</script>';
    
    require './views/components/header.php';
    
    require './views/components/navigation.php';
    
    require './views/add_alert.php';
    
    require './views/components/footer.php';