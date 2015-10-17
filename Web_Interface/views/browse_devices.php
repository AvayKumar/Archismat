<div class="container">
    <div class="table-responsive">
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Token</th>
                    <th>Time</th>
                </tr>
            </thead>
            <tbody>
    <?php 
    
        require_once './database/Database.php';
        
        $db = new Database();
    
        $sqlQuery = "SELECT * FROM `registered_devices` WHERE 1";
    
    if(  $query =  $db->queryDb( $sqlQuery ) ) {
        $i = 0;
        foreach ($query as $value) {
            
            $datetime = DateTime::createFromFormat( 'Y-m-d H:i:s',  $value['registration_time'] , new DateTimeZone( 'UTC' ) );
            
            echo '<tr><td>'.$i++.'</td><td>'.$value['token_id'].'</td><td>'.$datetime->format('d M Y, h:i A').'</td></tr>';
        }
        
    } else {
        echo '<tr><td colspan="3">No registered device found</td></tr>';
    }
    
 ?>
            </tbody>
            <tfoot>
                <tr>
                    <th>#</th>
                    <th>Token</th>
                    <th>Time</th>
                </tr>
            </tfoot>
        </table>
    </div>
</div>