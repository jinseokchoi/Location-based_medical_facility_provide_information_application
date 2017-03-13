<?php
    $conn = mysqli_connect("localhost", "root", "83192750", "app");
    
    if (mysqli_connect_errno($conn))  
    {  
       echo "Failed to connect to MySQL: " . mysqli_connect_error();  
    }  
    
    $city = $_POST['city'];
    
    mysqli_set_charset($conn,"utf8");
 
    $res = mysqli_query($conn, "select C_location, name, C_tel, C_manager, C_manager_tel, lat, lon from cardioverter where C_city='$city'");  
 
    $result = array();  
       
    while($row = mysqli_fetch_array($res)){  
      array_push($result,  
        array('C_location' =>$row[0], 'name'=>$row[1],'C_tel'=>$row[2], 'C_manager'=>$row[3], 'C_manager_tel'=>$row[4], 'lat'=>$row[5], 'lon'=>$row[6] 
        ));  
    }  
       
    echo json_encode(array("result"=>$result));  
       
    mysqli_close($conn);  
?>


