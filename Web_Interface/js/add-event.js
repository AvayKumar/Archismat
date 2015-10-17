$(function(){
    
    var data = [{'name':'op1', 'location':'Location 1', 'coord-x': '45', 'coord-y': '20'},
                {'name':'op2', 'location':'Location 2', 'coord-x': '55', 'coord-y': '36'},
                {'name':'op3', 'location':'Location 3', 'coord-x': '49', 'coord-y': '45'},
                {'name':'op4', 'location':'Location 4', 'coord-x': '23', 'coord-y': '82'},
                {'name':'op5', 'location':'Location 5', 'coord-x': '93', 'coord-y': '63'},
                {'name':'op6', 'location':'Location 6', 'coord-x': '25', 'coord-y': '48'},
                {'name':'op7', 'location':'Location 7', 'coord-x': '78', 'coord-y': '54'}];
    
    $('#form select[name="event-name"]').change(function(){
        
        var loc = $(this).val();
        var i;
        for(i=0; i<data.length; i++)
            if( data[i].name == loc )  {
                $('#form input[name="event-location"]').val(data[i].location);
                $('#form input[name="location-coord-x"]').val(data[i]['coord-x']);
                $('#form input[name="location-coord-y"]').val(data[i]['coord-y']);
                break;
            }
        
    });
    
});