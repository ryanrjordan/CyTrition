<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";
$conn = mysqli_connect($servername,$username,$password,$dbname);

if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['login_id']) && isset($_POST['recipe_name'])) {
	$loginid = $_POST['login_id'];
	$recipeName = $_POST['recipe_name'];

	$query = "INSERT INTO Recipe (login_id,recipe_name) VALUES ('$loginid', '$recipeName');";
	if($conn->query($query)===TRUE) {
		echo "yes, recipe name added to the database";
	} else {
		echo "Could't add to database";
	}
}


$conn->close();
exit;
?>
