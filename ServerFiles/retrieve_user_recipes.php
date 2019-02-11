<?php
$servername = "mysql.cs.iastate.edu";
$username = "dbu309yt8";
$password = "TarDx@ew";
$dbname = "db309yt8";

$conn = mysqli_connect($servername,$username,$password,$dbname);
if(!$conn) {
	die("Connection failed: " . mysqli_connect_error());
}

if(isset($_POST['login_id'])) {
	$login_id = $_POST['login_id'];
	$query = "SELECT recipe_name FROM Recipe WHERE BINARY login_id = '$login_id';";
	$result = mysqli_query($conn,$query);
	if ($result->num_rows > 0) {
		while($row =  mysqli_fetch_row($result)) {
	        echo "recipe_name," . $row[0] . ",";
		}
		//echo "recipe_names,nato,taco,rice,";
	}
	else {
		echo "Can't find recipe.";
	}
}

$conn->close();
exit;
?>
