function fillDataList() {
  var request = new XMLHttpRequest();
  request.open("GET", "./api/getPropertyTypes", false);//NOT ASYNC HERE!
  request.send()
  if (request.status!=200){
    alert('Error! Status='+request.status+"Text:"+request.responseText);
    return;
  }
  var theReply=request.responseText;
  if (!theReply){
    alert('Server error! Reply is null!');
    return;
  }
  var theResult;
  try{
    theResult = JSON.parse(theReply);
  }
  catch(exception){
    alert("Server error! Reply is not in JSON format!");
    return;
  }
  if (!theResult['error']){
    alert("Server error! Expected option not found!");
    return;
  }
  if (theResult['error'] !== -1){
    alert("Не удалось получить тип недвижимости. Ошибка:"+theResult['error']);
    return;
  }
  
  if (!theResult['propertyTypes']){
    alert("Неверный формат ответа сервера");
    return;
  }

  var datalistElem = document.getElementById("propertyTypesDatalist");
  for (var i=0; i<theResult['propertyTypes'].length; i++){
	  var option = document.createElement('option');
	  option.value= theResult['propertyTypes'][i];
	  datalistElem.appendChild(option);
  }
}
fillDataList();
