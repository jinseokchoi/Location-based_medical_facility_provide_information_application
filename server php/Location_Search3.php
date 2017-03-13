<?php
$con=mysqli_connect("localhost","root","83192750","app");
 
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
 
$name = $_POST['name'];
$lat = $_POST['lat'];
$lon = $_POST['lon'];
$dbchoice = $_POST['choice'];
$query = mysqli_query($con,"SELECT name, lat, lon , ( 6371 * acos( cos( radians(lat) ) * cos( radians($lat) )* cos( radians($lon) - radians(lon) ) + sin( radians(lat) ) * sin( radians($lat) ) ) ) AS distance from  '$dbtable'  HAVING distance < 3 LIMIT 0 , 999");

$result = array();

while($row = mysqli_fetch_array($query)){
  array_push($result,  
  array('name' => $row[0], 'lat' =>$row[1], 'lon'=>$row[2])); 
}

echo json_encode(array("result"=>$result));

mysqli_close($con);


?>