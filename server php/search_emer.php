<?php
    $conn = mysqli_connect("localhost", "root", "83192750", "app");
    
    if (mysqli_connect_errno($conn))  
    {  
       echo "Failed to connect to MySQL: " . mysqli_connect_error();  
    }  
    
    $city = $_POST['city'];
    
    mysqli_set_charset($conn,"utf8");
 
    $res = mysqli_query($conn, "select E_location, name, E_tel, lat, lon from emergency where E_location like '%$city%'");  
 
    $result = array();  
       
    while($row = mysqli_fetch_array($res)){  
      array_push($result,  
        array('E_location' =>$row[0], 'name'=>$row[1],'E_tel'=>$row[2],'lat'=>$row[3], 'lon'=>$row[4] 
        ));  
    }  
       
    echo json_encode(array("result"=>$result));  
       
    mysqli_close($conn);  
?>


