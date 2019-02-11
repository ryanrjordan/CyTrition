<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['meal_id']) && isset($_POST['recipe_id'])) {
	$meal_id = $_POST['meal_id'];
	$recipe_id = $_POST['recipe_id'];
	
	$query = "INSERT INTO meal_composition (meal_id,recipe_id) VALUES ('$meal_id', '$recipe_id');";
	if($conn->query($query)===TRUE) {
		echo "successful";
	} else {
		echo "Couldn't add to database";
	}
}

$conn->close();
exit;
?>
