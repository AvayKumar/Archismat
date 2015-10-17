<?php

    require './database/Database.php';
    
    $server_response = '';
    
    if( isset( $_POST['submit'] ) ) {
        
        $db = new Database();
        
        $alert_body = addslashes( $_POST['alert-description'] ) ;
        
        $postDateTime = $_POST['alert-date'].' '.$_POST['alert-time'];
        
        $datetime = DateTime::createFromFormat( 'Y-m-d H:i',  $postDateTime , new DateTimeZone( 'UTC' ) );
        $alert_datetime = $datetime->format( 'Y-m-d H:i' );    
                
        $update_type = 0;
        
        $insertQuery = "INSERT INTO `message`(`datetime`, `update_type`, `description`) VALUES ('{$alert_datetime}','{$update_type}','{$alert_body}')";
        
        if( $db->queryInsertDb($insertQuery) ) {
            $server_response = '<div class="alert alert-success" role="alert">Alert has been <strong>successfully</strong> added</div>';
        } else {
            $server_response = '<div class="alert alert-danger" role="alert">Something went wrong!!! Thats all we know</div>';
        }
        
    }
    
?>
<div class="container">
    <?php echo $server_response; ?>
    <form method="post" action="<?php echo BASE_URL.'add-alert.php' ?>" role="form">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="date" class="form-control" name="alert-date" required>
                                    <div class="input-group-addon">Publish Date</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <div class="input-group">
                                    <input type="time" class="form-control" name="alert-time" onchange="alert($(this).val())" required>
                                    <div class="input-group-addon">Publish Time</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                      <textarea class="form-control" name="alert-description" placeholder="Alert body" rows="5" required></textarea>
                    </div>
                    <input type="submit" name="submit" class="btn btn-primary" value="Submit">
                </form>
</div>