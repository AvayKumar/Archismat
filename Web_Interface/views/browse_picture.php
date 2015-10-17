<div class="container">
    <script>
        
        function confirmDelete( obj ) {
            
            if( confirm('Are you sure you want to delete this item?') ) {
               window.location = '<?php echo BASE_URL.'browse-pick.php?del=' ?>' + $(obj).attr('data-id');
            }
        }
        
    </script>
    
    <?php 
    
        require_once './database/Database.php';
        
        $db = new Database();
    
    /*
          * Process delete query
          **/
    
    if( isset( $_GET['del'] ) ) {

        $deleteQuery = "DELETE FROM `message` WHERE `message_id` = {$_GET['del']}";

        if( $db->queryDeleteDb( $deleteQuery ) ) {
            echo '<div class="alert alert-success" role="alert">Picture has been <strong>successfully</strong> deleted</div>';
        } else {
            echo '<div class="alert alert-danger" role="alert">Something went wrong!!! Thats all we know</div>';
        }

    }
        
    ?>
    
    <div class="table-responsive">
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th style="width: 100px">Picture</th>
                    <th>Description</th>
                    <th>Sending Time</th>
                    <th>Status</th>
                    <th>Edit/ Delete</th>
                </tr>
            </thead>
            <tbody>
<?php
    
    $sqlQuery = "SELECT `message_id`, `datetime`, `description`, `pick`, `sent` FROM `message` WHERE `update_type` = '2' ORDER BY `sent`, `datetime`";
    
    if(  $query =  $db->queryDb( $sqlQuery ) ) {
        
        foreach ($query as $value) {
            
            $datetime = DateTime::createFromFormat( 'Y-m-d H:i:s',  $value['datetime'] , new DateTimeZone( 'UTC' ) );
            
            if( $value['sent'] ) {
                $lable = '<span class="label label-danger">Sent</span>';
                $button = '<a class="btn btn-xs btn-warning disabled">Edit</a> <a class="btn btn-xs btn-danger disabled">Delete</a>';
            } else {
                $lable = '<span class="label label-success">Pending</span>';
                $button = '<a href="edit-pick.php?id='.$value['message_id'].'" class="btn btn-xs btn-warning">Edit</a> <a href="javascript:void(0);" data-id="'.$value['message_id'].'" class="btn btn-xs btn-danger" onclick="confirmDelete(this)">Delete</a>';
            }
            //echo '<tr><td>'.$value['description'].'</td><td>'.$datetime->format('d M Y, h:i A').'</td><td>'.$lable.'</td><td>'.$button.'</td></tr>';
            echo '<tr><td><img src="'.BASE_URL.'uploads/files/thumbnail/'.$value['pick'].'"></td>'
                    .'<td style="vertical-align: middle">'.$value['description'].'</td>'
                    .'<td style="vertical-align: middle">'.$datetime->format('d M Y, h:i A').'</td>'
                    .'<td style="vertical-align: middle">'.$lable.'</td>'
                    .'<td style="vertical-align: middle">'.$button.'</td></tr>';
        }
        
    } else {
        echo '<tr><td colspan="4">Nothing has been added yet</td></tr>';
    }
    
 ?>
            </tbody>
            <tfoot>
                <tr>
                    <th>Picture</th>
                    <th>Description</th>
                    <th>Sending Time</th>
                    <th>Status</th>
                    <th>Edit/ Delete</th>
                </tr>
            </tfoot>
        </table>
    </div>
</div>