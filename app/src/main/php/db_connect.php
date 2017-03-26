<?php
	$con = mysqli_connect("localhost","pricyfy1_pradmin","asdfghjkl123456789","pricyfy1_pricyfy");
	if(mysqli_connect_errno())
	{
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
	$id = $_GET["id"];
	$sql = "Select * from product where ((id between ($id+1) and ($id+100)) AND (category_id = 1))";
	$result = mysqli_query($con,$sql);
	while($row = mysqli_fetch_assoc($result))
	{
		$array[]=$row;
	}
	header('Content-Type : Application/json');
	echo json_encode($array);
	mysqli_free_result($result);
	mysqli_close($con);
?>
