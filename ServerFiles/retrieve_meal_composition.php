<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);
if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['meal_id'])) {
	$meal_id = $_POST['meal_id'];
	$query = "SELECT * FROM meal_composition WHERE BINARY meal_id = '$meal_id'";
	$result = mysqli_query($conn,$query);
	$recipe_id_array = array();
	if($result->num_rows > 0) {
		while($row =  mysqli_fetch_row($result)) {
	        array_push($recipe_id_array, $row[1]);
		}
	} else {
		echo "Can't find any meal data.";
	}
	
	foreach($recipe_id_array as $value) {
		$second_query = "SELECT recipe_name FROM Recipe WHERE BINARY recipe_id = '$value'";
		$second_result = mysqli_query($conn,$second_query);
		$currow =  mysqli_fetch_row($second_result);
		echo $currow[0] . ",";
	}
}

$conn->close();
exit;
?>