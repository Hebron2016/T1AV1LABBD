CREATE DATABASE Avaliacao1LABBDclinicaV6
GO
USE Avaliacao1LABBDclinicaV6

CREATE TABLE pessoa(
rg			CHAR(9) NOT NULL,
nome		VARCHAR(100) NOT NULL,
telefone	CHAR(11) NOT NULL,
estado		BIT NOT NULL
PRIMARY KEY (rg)
)
GO
CREATE TABLE especialidade(
codigo		INT NOT NULL,
nomeEsp		VARCHAR(100) NOT NULL
PRIMARY KEY (codigo)
)
GO
CREATE TABLE cliente(
pessoarg	CHAR(9) NOT NULL,
dataNasc	DATE NOT NULL,
senha		CHAR(8) NOT NULL
PRIMARY KEY (pessoarg)
FOREIGN KEY (pessoarg) REFERENCES Pessoa(rg)
)
GO
CREATE TABLE medico(
pessoarg	CHAR(9) NOT NULL,
especialidadeCodigo INT NOT NULL
PRIMARY KEY (pessoarg)
FOREIGN KEY (pessoarg) REFERENCES Pessoa(rg),
FOREIGN KEY (especialidadeCodigo) REFERENCES Especialidade(codigo)
)
GO
CREATE TABLE planoDeSaude(
codAutorizacao	INT NOT NULL,
vlrParaMedico	DECIMAL(6,2) NOT NULL,
PRIMARY KEY(codAutorizacao)
)
GO
CREATE TABLE consulta(
dataConsulta	DATE NOT NULL,
hora			TIME(4) NOT NULL,
clientePessoarg CHAR(9) NOT NULL,
medicoPessoarg	CHAR(9) NOT NULL,
pscodAutorizacao INT NULL,
tipoConsulta	CHAR(2) NOT NULL,
vlrTotal		DECIMAL(6,2) NOT NULL
PRIMARY KEY(dataConsulta, hora,clientePessoarg)
FOREIGN KEY(clientePessoarg) REFERENCES Cliente(Pessoarg),
FOREIGN KEY(medicoPessoarg) REFERENCES Medico(Pessoarg),
FOREIGN KEY (pscodAutorizacao) REFERENCES planoDeSaude(codAutorizacao)
)
GO
CREATE TABLE material(
id				INT	NOT NULL,
nome			VARCHAR(100) NOT NULL,
valor			DECIMAL(5,2) NOT NULL
PRIMARY KEY(id)
)
GO
CREATE TABLE consulta_Material(
consultaData	DATE NOT NULL,
consultaHora	TIME(4) NOT NULL,
consultaClientePessoaRg	CHAR(9) NOT NULL,
materialId		INT NOT NULL,
quantidade		INT NULL,
vlrTotalMaterial INT NULL
PRIMARY KEY (consultaData, consultaHora, consultaClientePessoaRg, materialId),
FOREIGN KEY (consultaData, consultaHora, consultaClientePessoaRg) REFERENCES Consulta(dataConsulta, hora, clientePessoarg),
FOREIGN KEY (materialId) REFERENCES Material(Id)
)

---------STORED PROCEDURES------------
--------------------------------------

CREATE PROCEDURE sp_vldIdade(@cdata DATE, @vld BIT OUTPUT)
AS
	DECLARE @ano INT,
		@mes int,
		@dia int
	SET @ano = DATEDIFF(YEAR, @cdata, GETDATE())
	SET @mes = MONTH(@cdata)
	SET @dia = DAY(@cdata)
	SET @vld = 0
	IF (@ano <= 18)
	BEGIN
		IF ( @mes = MONTH(GETDATE()))
		BEGIN
			IF (@dia > DAY(GETDATE()))
			BEGIN
				RAISERROR('Menores de idade não pode se cadastrar', 16, 5)
			END
			ELSE
			BEGIN
				SET @vld = 1
			END
		END
		IF ( @mes > MONTH(GETDATE()))
		BEGIN
			RAISERROR('Menores de idade não pode se cadastrar', 16, 5)
		END
		IF ( @mes < MONTH(GETDATE()))
		BEGIN
			SET @vld = 1
		END
	END
	ELSE
	BEGIN
		SET @vld = 1
	END


CREATE PROCEDURE sp_umMesParaAgendar(@datacon DATE, @boolean BIT OUTPUT)
AS
	DECLARE	@numDay int
	SET @numday = DATEDIFF(DAY,GETDATE(),@datacon)
	IF(@numday > 30)
	BEGIN
		SET @boolean = 1
	END
	ELSE
	BEGIN
		SET @boolean = 0
		RAISERROR('A consulta deve ser realizadas apartir de um mes da data de hoje', 16,3)
	END




CREATE PROCEDURE sp_retornoConsulta ( @conNova DATE, @conNewEsp VARCHAR(100), @rgPa CHAR(9), @boolmenosMes BIT OUTPUT)
AS
	DECLARE @conAnterior DATE,
			@comparaData DATE,
			@segundaConAnterior DATE,
			@verificaSpecialidade VARCHAR(100)
	SET @boolmenosMes = 0
	SET @conAnterior = (SELECT TOP(1) dataConsulta FROM consulta WHERE clientePessoarg = @rgPa ORDER BY(dataConsulta) DESC)
	SET @verificaSpecialidade = (SELECT TOP(1) e.nomeEsp FROM consulta c, medico m, especialidade e 
								WHERE c.clientePessoarg = @rgPa 
								AND c.medicoPessoarg = m.pessoarg
								AND m.especialidadeCodigo = e.codigo
								AND e.nomeEsp = @conNewEsp
								ORDER BY(dataConsulta) ASC)
	SET @comparaData = DATEADD(DAY,30, @conAnterior)
	IF (GETDATE() <= @comparaData)
	BEGIN
		IF(@conNova <= @comparaData AND @conNova >= GETDATE())
		BEGIN
			IF (@verificaSpecialidade IS NOT NULL)
			BEGIN
				SET @boolmenosMes = 1
			END
			ELSE
			BEGIN
				RAISERROR('Não é possível fazer o retorno da consulta em uma especialidade diferente da anterior', 16, 5)
			END
		END
		ELSE
		BEGIN
			RAISERROR('Não é possível fazer o retorno da consulta em datas passadas ou em data fora do intervalo de retorno', 16, 5)
		END
	END
	ELSE
	BEGIN
		RAISERROR('O tempo para usar o retorno de consulta é já passou', 16, 5)
	END




CREATE PROCEDURE sp_vldRg(@rgvld CHAR(9), @bitvldRg BIT OUTPUT)
AS
	IF (LEN(@rgvld) != 9)
	BEGIN
		RAISERROR('O RG deve ter extamente 9 digitos ',16,3)
	END
	ELSE
	BEGIN
		DECLARE @TestSeCharInt INT,
				@contador	INT,
				@numeroRg	INT,
				@contCasa	INT,
				@somatorio	INT, 
				@digito		INT
		SET @bitvldRg = 0
		SET @TestSeCharInt = CAST(@rgvld AS int)
		SET @numeroRg = SUBSTRING(@rgvld, 1,1)
		SET @contCasa = 0
		SET @contador = 9
		SET @somatorio = 0
		SET @digito = SUBSTRING(@rgvld, 9,1)
		WHILE (@contador > 1)	
		BEGIN
			SET @contCasa = @contCasa + 1
			SET @numeroRg = SUBSTRING(@rgvld, @contCasa,1)
			SET @somatorio = (@numeroRg * @contador) + @somatorio
			SET @contador = @contador - 1	
		END
		IF((@somatorio % 11) = @digito)
		BEGIN
			SET @bitvldRg = 1
		END
		ELSE
		BEGIN
			RAISERROR('Esse rg é invalido',16,1)
		END
	END

CREATE  PROCEDURE sp_logar(@registro CHAR(9), @senha VARCHAR(255), @permissaoLogin BIT OUTPUT)
AS
	SET @permissaoLogin = 0
	IF(EXISTS(SELECT * FROM cliente WHERE pessoarg = @registro AND senha = @senha))
	BEGIN
		PRINT('LOGADO COM SUCESSO')
		SET @permissaoLogin = 1
	END
	ELSE
	BEGIN
		RAISERROR('O login ou a senha não coferem', 16,1)
	END

CREATE PROCEDURE sp_terceiraConsulta ( @conTer DATE, @conTerEsp VARCHAR(100), @rgCli CHAR(9), @boolTer BIT OUTPUT)
AS
	DECLARE @consul1 DATE,
			@consul2 DATE,
			@vldSpecia VARCHAR(100),
			@vldSpecia2 VARCHAR(100),
			@diasDistance INT,
			@spb	BIT
	SET @boolTer = 0
	SET @consul1 = (SELECT TOP(1) dataConsulta FROM consulta WHERE clientePessoarg = @rgCli ORDER BY(dataConsulta) DESC)
	PRINT(@consul1)
	SET @consul2 = (SELECT TOP(1) dataConsulta FROM consulta WHERE clientePessoarg = @rgCli AND dataConsulta != @consul1 ORDER BY(dataConsulta) DESC)
	PRINT(@consul2)
	SET @vldSpecia = (SELECT TOP(1) e.nomeEsp FROM consulta c, medico m, especialidade e 
							WHERE c.clientePessoarg = @rgCli 
							AND c.medicoPessoarg = m.pessoarg
							AND m.especialidadeCodigo = e.codigo
							AND e.nomeEsp = @conTerEsp
							ORDER BY(dataConsulta) DESC)
	SET @vldSpecia2 = (SELECT TOP(1) e.nomeEsp FROM consulta c, medico m, especialidade e 
							WHERE c.clientePessoarg = @rgCli 
							AND c.medicoPessoarg = m.pessoarg
							AND m.especialidadeCodigo = e.codigo
							AND e.nomeEsp = @conTerEsp
							AND c.dataConsulta != @consul1
							ORDER BY(dataConsulta) DESC)
	IF(@consul1 IS NOT NULL AND @consul2 IS NOT NULL AND @vldSpecia = @vldSpecia2 AND @vldSpecia = @conTerEsp)
	BEGIN
		EXEC sp_retornoConsulta @conTer, @conTerEsp,@rgCli,@spb OUTPUT
		IF(@spb = 1)
		BEGIN
			SET @boolTer = 1
		END
		ELSE
		BEGIN
			RAISERROR('Não foi possível efetuar o retorno', 16,2)
		END
	END



CREATE PROCEDURE sp_senhaForte(@senhaP CHAR(8), @vldforte BIT OUTPUT)
AS
	IF(LEN(@senhaP)< 8)
	BEGIN
		RAISERROR('A sua senha deve conter 8 caracteres incluindo letras e numeros',16,2)
	END
	ELSE
	BEGIN
		DECLARE @subSenha CHAR(8),	
				@posicao  INT,
				@contaSenha INT,
				@valnum	BIT,
				@valChar BIT,
				@verinum BIT
		SET @contaSenha = 0
		SET @valChar = 0
		SET @valnum = 0
		SET @posicao = 1
		WHILE (@contaSenha < 8)
		BEGIN
			SET @subSenha = (SUBSTRING(@senhaP, @posicao, 1))
			PRINT (@subSenha) 
			SET @verinum = 	ISNUMERIC(@subSenha)
			PRINT (@verinum)
			IF (@verinum = 1)
			BEGIN
				SET @valnum = 1
			END
			ELSE
			BEGIN
				SET @valChar = 1
			END
			SET @contaSenha = @contaSenha + 1
			SET @posicao = @posicao + 1
		END
		IF (@valChar =  1 AND @valnum = 1)
		BEGIN
			SET @vldforte = 1
		END
		IF (@valChar = 0)
		BEGIN
			RAISERROR('A sua senha deve conter letras', 16, 2)
		END
		IF (@valnum = 0)
		BEGIN
			RAISERROR('A sua senha deve conter pelo menos 1 número',16,2)
		END
	END

CREATE PROCEDURE sp_cadConsulta (@dataDaConsulta DATE, @horaConsulta TIME(4), @rgPaciente CHAR(9),@espNome VARCHAR(100),
									@rgMedico CHAR(9), @codigoAut INT NULL, @typeConsulta CHAR(2), @vlr DECIMAL(6,2))
AS
	DECLARE @vldRg CHAR(9),
			@contConsul INT,
			@especialidadeMedico VARCHAR(100),
			@vldPossibi	INT,
			@anterior DATE,
			@bitUm	BIT,
			@bitDois BIT,
			@bitTres BIT
		SET @vldRg = (SELECT pessoarg FROM cliente Where pessoarg = @rgPaciente)
		IF(@vldRg IS NULL)	
		BEGIN
			RAISERROR('Esse RG ainda não foi cadastrado', 16,2)
		END 
		ELSE
		BEGIN
			SET @vldRg = (SELECT pessoarg FROM medico Where pessoarg = @rgMedico)
			IF(@vldRg IS NULL)
			BEGIN
				RAISERROR('Esse médico não existe', 16,3)
			END		
			ELSE
			BEGIN	
				SET @contConsul =  (SELECT COUNT(clientePessoarg) FROM consulta WHERE clientePessoarg = @rgPaciente)
				SET @anterior = (SELECT TOP(1)dataConsulta FROM consulta WHERE clientePessoarg = @rgPaciente ORDER BY dataConsulta DESC )
				SET @vldPossibi = DATEDIFF(DAY,@anterior, GETDATE())
				IF (@contConsul = 0 OR @vldPossibi > 30 )
				BEGIN 
					EXEC sp_umMesParaAgendar @dataDaConsulta, @bitUm OUTPUT
					IF(@bitUm = 1)
					BEGIN
						INSERT INTO consulta VALUES
						(@dataDaConsulta, @horaConsulta, @rgPaciente, @rgMedico, @codigoAut,@typeConsulta, @vlr)
					END
					ELSE
					BEGIN
						RAISERROR('Não foi possivel realizar a tente outra data após 30 dias corridos', 16,5)
					END 
				END
				IF(@contConsul = 1)
				BEGIN
					SET @especialidadeMedico = (SELECT e.nomeEsp FROM medico m, especialidade e  
													WHERE m.especialidadeCodigo = e.codigo
													AND	m.pessoarg = @rgMedico)
					EXEC sp_retornoConsulta @dataDaConsulta, @especialidadeMedico, @rgPaciente, @bitDois OUTPUT
					IF(@bitDois = 1)
					BEGIN
						INSERT INTO consulta VALUES
						(@dataDaConsulta, @horaConsulta, @rgPaciente, @rgMedico, @codigoAut,@typeConsulta, @vlr)
					END
					ELSE
					BEGIN
						RAISERROR('Não foi possivel realizar a tente outra data após 30 dias corridos', 16,5)
					END 
				END
				IF (@contConsul >= 2)
				BEGIN
					SET @anterior = (SELECT TOP(1)dataConsulta FROM consulta WHERE clientePessoarg = @rgPaciente ORDER BY dataConsulta DESC )
					SET @vldPossibi = DATEDIFF(DAY,@anterior, GETDATE())
					IF (@vldPossibi <= 30)
					BEGIN
						PRINT('A ZONA DO LENDARIO BIT 3')
						EXEC sp_terceiraConsulta @dataDaConsulta, @espNome, @rgPaciente, @bitTres OUTPUT
						IF(@bitTres = 1)
						BEGIN
							INSERT INTO consulta VALUES
							(@dataDaConsulta, @horaConsulta, @rgPaciente, @rgMedico, @codigoAut,@typeConsulta, @vlr)
						END
						ELSE
						BEGIN
							RAISERROR('Não foi possivel realizar a tente outra data após 30 dias corridos', 16,5)
						END 
					END
				END
			END
		END

CREATE PROCEDURE sp_cadCliente (@registro CHAR(9), @clNome VARCHAR(100), @numtelefone CHAR(11), @dn DATE, @pwd CHAR(8))
AS
	DECLARE @birg BIT,
			@biIdade BIT,
			@biSenha BIT
			SET @birg = 0
			SET @biIdade = 0
			SET @biSenha = 0
			IF (LEN(@numtelefone) = 11)
			BEGIN
				EXEC sp_vldIdade @dn, @biIdade OUTPUT
				IF(@biIdade = 1)
				BEGIN
					EXEC sp_vldRg @registro, @birg OUTPUT
					IF (@birg = 1)
					BEGIN			
						EXEC sp_senhaForte @pwd, @biSenha OUTPUT
						IF (@biSenha = 1)
						BEGIN
							INSERT INTO pessoa VALUES
							(@registro,@clNome,@numtelefone,@birg)
							INSERT INTO cliente VALUES
							(@registro,@dn, @pwd)
						END
					END
				END
			END
			ELSE
			BEGIN
				RAISERROR('O numero de telefone é invalido, por favor insira um numero de telefone com 11 digitos', 16,5)
			END

CREATE PROCEDURE sp_cadMed (@regMed CHAR(9), @nomeMed VARCHAR(100), @telMed CHAR(11), @codEsp INT)
AS
	DECLARE @birm BIT
		EXEC sp_vldRg @regMed,@birm OUTPUT
		IF (@birm = 1)
		BEGIN
			INSERT INTO pessoa VALUES
			(@regMed,@nomeMed,@telMed,@birm)
			INSERT INTO medico VALUES
			(@regMed, @codEsp)
		END

--ZONA DE TESTES--
--SERÁ FEITO TESTES AVL

---TESTE PROCEDURE sp_vldIdade---
DECLARE @test BIT
EXEC sp_vldIdade '2006-09-26', @test
GO
DECLARE @test BIT
EXEC sp_vldIdade '2006-09-27', @test
GO
DECLARE @test BIT
EXEC sp_vldIdade '2006-09-28', @test
GO
DECLARE @test BIT
EXEC sp_vldIdade '2006-10-27', @test
GO
DECLARE @test BIT
EXEC sp_vldIdade '2005-11-27', @test
GO
DECLARE @test BIT
EXEC sp_vldIdade '2007-10-27', @test


---Teste Procedure sp_umMesParaAgenda---
DECLARE @test BIT
EXEC sp_umMesParaAgendar '2024-09-24', @test
GO
DECLARE @test BIT
EXEC sp_umMesParaAgendar '2024-07-24', @test
GO
DECLARE @test BIT
EXEC sp_umMesParaAgendar '2023-07-24', @test

---Teste Procedure sp_retornoConsulta---
DECLARE @test BIT
EXEC sp_retornoConsulta '2024-09-27', 'geral','123456789',@test
GO
DECLARE @test BIT
EXEC sp_retornoConsulta '2024-10-27', 'lalandi','123456789',@test
GO
DECLARE @test BIT
EXEC sp_retornoConsulta '2024-10-27', 'geral','123456789',@test

---Teste Procedure sp_vldRg---
DECLARE @test BIT
EXEC sp_vldRg 'meucueteu', @test

DECLARE @test BIT
EXEC sp_vldRg '123456789', @test

DECLARE @test BIT
EXEC sp_vldRg '594138747', @test

DECLARE @test BIT
EXEC sp_vldRg '59417', @test

---Teste Procedure sp_logar---
DECLARE @test BIT
EXEC sp_logar '121241123', '12314', @test

---Teste Procedure sp_senhaForte
DECLARE @test BIT
EXEC sp_senhaForte 'j12312', @test
GO
DECLARE @test BIT
EXEC sp_senhaForte 'asaddada', @test
GO
DECLARE @test BIT
EXEC sp_senhaForte 'asaddadaaa', @test

---Teste Terceiro Mês---
DECLARE @test BIT
EXEC sp_terceiraConsulta '2024-09-30', 'geral','123456789',@test

---Teste sp_cadConsulta---
EXEC sp_cadConsulta '2024-09-30', '02:00', '123789123', '123456789',NULL, 'PA',1000.00
EXEC sp_cadConsulta '2024-09-30', '02:00', '123789123', '123456789',NULL, 'PA',1000.00
EXEC sp_cadConsulta '2024-10-01', '02:00', '123456798', '124123456',NULL, 'PA',1000.00
EXEC sp_cadConsulta '2024-10-12', '02:00', '123456798','geral', '124123456',NULL, 'PA',1000.00

---Teste sp_cadCliente---
EXEC sp_cadCliente '594138747', 'Henrique', '11968096127', '2004-03-01', 'Jagunso1' 

---MASSA DE DADOS---

INSERT INTO pessoa VALUES
('123456789','god of redo','11968096127',1)

INSERT INTO cliente VALUES
('123456789','2004-03-01','jansulo1')

INSERT INTO pessoa VALUES
('124123456','Kratos','11968096127',1)

INSERT INTO especialidade VALUES
(1,'geral')

INSERT INTO medico VALUES
('124123456',1)

INSERT INTO consulta VALUES
('2024-09-29', '02:00','123456789','124123456', NULL,'PA',1000.00)
GO
INSERT INTO consulta VALUES
('2024-09-12', '02:00','123456789','124123456', NULL,'PA',1000.00)

INSERT INTO pessoa VALUES
('123789123','JAGUNSO','11968096127',1)

INSERT INTO cliente VALUES
('123789123','2003-02-20','JILMARX1')

INSERT INTO pessoa VALUES
('123456798', 'VALMIR', '11968096127',1)

INSERT INTO cliente VALUES
('123456798', '2003-02-20','ZAZALLL7')

INSERT INTO consulta VALUES
('2024-09-12', '02:00','123456798','124123456', NULL,'PA',1000.00)

SELECT * FROM pessoa
SELECT * FROM cliente
SELECT * FROM especialidade
SELECT * FROM medico
SELECT * FROM consulta ORDER BY dataConsulta DESC