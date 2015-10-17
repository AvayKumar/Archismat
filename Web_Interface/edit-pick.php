<?php
    
    session_start();
    
    if( !isset( $_SESSION['admin'] ) || !$_SESSION['admin'] ) {
        header('Location: index.php');
    }
        

    $css_links = '<link href="./css/bootstrap.min.css" rel="stylesheet">';
    $js_links = $js_links = '<script src="./js/jquery-1.10.2.min.js"></script>'
              . '<script src="./js/jquery.ui.widget.js"></script>'
              . '<script src="./js/jquery.iframe-transport.js"></script>'
              . '<script src="./js/jquery.fileupload.js"></script>'
              . '<script src="./js/bootstrap.min.js"></script>'
              . '<script src="./js/image-upload.js"></script>'
              . '<script>$(function(){ $("#navbar>ul>li:nth-child(3)").addClass("active"); })</script>';



    require './views/components/header.php';

    require_once './database/Database.php';
        
    $db = new Database();
    
    $server_response = '';
    
    if( isset( $_POST['submit'] ) ) {
        
        $db = new Database();
        
        $image_name = htmlentities( $_POST['image-name'] ) ;
        
        $postDateTime = $_POST['pick-date'].' '.$_POST['pick-time'];
        
        $datetime = DateTime::createFromFormat( 'Y-m-d H:i',  $postDateTime , new DateTimeZone( 'UTC' ) );
        $pick_datetime = $datetime->format( 'Y-m-d H:i:s' );
        
        $pick_description = htmlentities( $_POST['pick-description'] ) ;
        
        $update_type = 2;
                
        //$insertQuery = "INSERT INTO `message`(`datetime`, `update_type`, `description`, `pick`) VALUES ('{$pick_datetime}','{$update_type}','{$pick_description}','{$image_name}')";
        $sqlQuery = "UPDATE `message` SET `datetime`='{$pick_datetime}',`description`='{$pick_description}',`pick`='{$image_name}' WHERE `message_id` = '{$_GET['id']}'";
        
        if( $db->queryUpdateDb( $sqlQuery ) ) {
            $server_response = '<div class="alert alert-success" role="alert">Image  update has been <strong>successfully</strong> updated</div>';
        } else {
            echo mysqli_error(Database::$conn);
            $server_response = '<div class="alert alert-danger" role="alert">Something went wrong!!! Thats all we know</div>';
        }
        
    }

    
    
    if( isset( $_GET['id'] ) ) {
        
        $sqlQuery = "SELECT `datetime`, `update_type`, `description`, `pick` FROM `message` WHERE `message_id` = '{$_GET['id']}'";
        
        if( $query = $db->queryDb( $sqlQuery ) ) {
            
            $row = $query[0];
            
            if( $row['update_type'] == 2 ) {
                
                $datetime = DateTime::createFromFormat( 'Y-m-d H:i:s',  $row['datetime'] , new DateTimeZone( 'UTC' ) );
                
                $pick_url = BASE_URL.'uploads/files/preview/'.$row['pick'];
                $pick_name = $row['pick'];
                $pick_date = $datetime->format('Y-m-d');
                $pick_time = $datetime->format('H:i');
                $pick_description = $row['description'];
                
                
            } else {
                die('<div class="container text-center"><h1>Invalid alert id.</h1></div>');
            }
            
        }
        
        require './views/components/navigation.php';
        require './views/edit_pick.php';
        
    } else {
        die('<div class="container text-center"><h1>Invalid alert id.</h1></div>');
    }
    
    require './views/components/footer.php';
    


