var openContract_btn = document.getElementById("openContract_btn");
if (openContract_btn){
	openContract_btn.onclick = function() {
		alert('Этот функционал не был внесен в задание')
	}
}

var createContract_btn = document.getElementById("createContract_btn");
if (createContract_btn){
	createContract_btn.onclick = function() {
		window.location.href = "./CreateContract.html";
	  }
}


function analyzeResult(request){
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
		  alert("Не удалось получить данные. Ошибка:"+theResult['error']);
		  return;
	  }
	  
	    /*
     * shortContracts = {"contractNumber" : ..., "contractDate":..., 
     *      "FIO":"...", "award":..., "activeFrom":"...", "activeTo":...}
     *      activeFrom, activeTo - check for presence
	     */
	  var errorMsg;
	  var theTable = document.getElementById("contractsTable");
	  try{
		  for  (var i = 0; i<theResult['shortContracts'].length; i++){
			  var row = document.createElement('tr');
			  var contract = theResult['shortContracts'][i];	
			  
			  var contractNumberTd = document.createElement('td');
			  contractNumberTd.innerText = contract["contractNumber"];
			  row.appendChild(contractNumberTd);
			  var contractDateTd = document.createElement('td');
			  contractDateTd.innerText = (new Date(contract["contractDate"])).toLocaleDateString();
			  row.appendChild(contractDateTd);
			  var FIOTd = document.createElement('td');
			  FIOTd.innerText = contract["FIO"];
			  row.appendChild(FIOTd);
			  var awardTd = document.createElement('td');
			  awardTd.innerText = contract["award"];
			  row.appendChild(awardTd);
			  
			  var activePeriodTd = document.createElement('td');
			  if (contract['activeFrom'] && contract['activeTo']){
				  activePeriodTd.innerText = (new Date(contract['activeFrom'])).toLocaleDateString() +"-"+
				  	(new Date(contract['activeTo'])).toLocaleDateString();  
			  }
			  else {
				  activePeriodTd.innerText = 'Нет данных';
			  }
			  row.appendChild(activePeriodTd);
			  
			  theTable.appendChild(row);
		  }
	  }
	  catch (e) {
		  errorMsg = "Ошибка! " + e;
	  }
	  if (errorMsg){
		  alert('Неверный формат данных!' + errorMsg);  
	  }
}	

analyzeResult(request);

