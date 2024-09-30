<%@	taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%-- <link rel="stylesheet" href="./css/styles.css">--%>
<title>Especialidade</title>
</head>
<body>
	<div>
		<jsp:include page="menu.jsp" />
	</div>
	<br />
	<div align="center" 
	<%---  class="container"---%>
	>
		<form action ="especialidade" method="post">
			<p <%--- class="title" "---%>> 
				<b>Especialidade</b>
			</p>
			<table>
				<tr>
					<td colspan="3">
						<input class="id_input_data" type="number" min="0" step="1" id="codigo" 
								name="codigo" placeholder="Codigo"
								value='<c:out value="${especialidade.codigo }"></c:out>'>
					</td>	
					<td>
						<input type="submit" id="botao" name="botao" value="Buscar" >
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<input class="input_data" type="text" id="nome" name="nome" placeholder="Nome" 
						value='<c:out value="${especilidade.nome }"></c:out>'>
					</td>
				</tr>	
				<tr>
					<td>
						<input type="submit" id="botao" name="botao" value="Cadastrar" >
					</td>
					<td>
						<input type="submit" id="botao" name="botao" value="Alterar" >
					</td>
					<td>
						<input type="submit" id="botao" name="botao" value="Excluir" >
					</td>
					<td>
						<input type="submit" id="botao" name="botao" value="Listar" >
					</td>
				</tr>
			</table>
		</form>	
	</div>
	<br />
	<div align="center">
		<c:if test="${not empty erro }">
			<H2><b><c:out value="${erro}" /></b></H2>
		</c:if>
	</div>
		<div align="center">
		<c:if test="${not empty saida }">
			<H3><b><c:out value="${saida}" /></b></H3>
		</c:if>
	</div>
	<br />
	<div align="center">
		<c:if test="${not empty especialidades }">
			<table <%--- class="table_round""---%>>
				<thead>
					<tr>
						<th>Codigo</th>
						<th>Nome </th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="e" items="${especialidades }">
					<tr>
						<td><c:out value="${e.codigo }"/></td>
						<td><c:out value="${e.nome }"/></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>	
		</c:if>
	</div>	
</body>
</html>