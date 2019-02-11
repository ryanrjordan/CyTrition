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

	$query = "SELECT recipe_id FROM Recipe WHERE BINARY login_id = '$login_id' && recipe_name = '$recipe_name';";
    $result = mysqli_query($conn,$query)
    if ($result->num_rows > 0) {
		while($row =  mysqli_fetch_row($result)) {
	        echo $row[0];
		}
	}
	else {
		echo "Can't find recipe_id.";
	}

}


$conn->close();
exit;
?>
