<?php
    $conn = mysqli_connect("localhost", "root", "83192750", "app");
    
    if (mysqli_connect_errno($conn))  
    {  
       echo "Failed to connect to MySQL: " . mysqli_connect_error();  
    }  
    
    $city = $_POST['city'];
    
    mysqli_set_charset($conn,"utf8");
 
    $res = mysqli_query($conn, "select H_location, H_kind, name, H_tel, H_weekday, H_weekend, lat, lon from hospital where H_location like '%$city%'");  
 
    $result = array();  
       
    while($row = mysqli_fetch_array($res)){  
      array_push($result,  
        array('H_location' =>$row[0], 'H_kind' =>$row[1], 'name'=>$row[2],'H_tel'=>$row[3], 'H_weekday' => $row[4], 'H_weekend' => $row[5], 'lat'=>$row[6], 'lon'=>$row[7] 
        ));  
    }  
       
    echo json_encode(array("result"=>$result));  
       
    mysqli_close($conn);  
?>


