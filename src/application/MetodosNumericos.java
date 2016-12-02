package application;

import java.util.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import org.nfunk.jep.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.lsmp.djep.djep.*;

public class MetodosNumericos{
	public double[] valoresx ;//= {1,2,3,4,5,6,7};
	public double[] valoresy ;//= {1,1.5,2.5,4,6,9,15};
	public double[] valoresy2 ;//= {1,1.44,1.96,2.56,3.24,4};
	public double ultimaX=-Double.MAX_VALUE;//=0;
	public DJep P; 
	public DJep diff;
	public DJep diff2;
	public double[][] Matriz; //= {{4,-1,1,3},{-1,4,1,-2},{2,1,5,3}}; 
	public String ultimaFunc=""; //= "3*x*x-5";
	public String ultimaFuncY="";// = "2*x*y";
	public double ultimaH=-Double.MAX_VALUE;// = .2;
	public double ultimaN=-Double.MAX_VALUE;// = 6;
	public double errorPermisible=-Double.MAX_VALUE;//=0.001;
	public double ultimaX1=-Double.MAX_VALUE;//=1;
	public double ultimaX2=-Double.MAX_VALUE;//=2;
	//public public double[][] = new double[][];
	
	public MetodosNumericos(){
		P = InitParser();
		diff = InitParser();
		diff2 = InitParser();
	}

	

	public ObservableList<Iteracion> BI(double a,double b,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		double xm,error;
		int i=1;
		do {
			//ENCUENTRA PUNTO MEDIO
			xm = (b+a)/2;
			
			double fa = f(a);
			double fxm = f(xm);
			
			if (fxm*fa==0){
				xm = ((fxm==0)?xm:a);
				//COMPARA SI BOLZANO DA 0 ENTONCES LA RAIZ ES EXACTA EN EL PUNTO MEDIO O EN EL PUNTO 1.
				res.setText("Raiz exacta: "+xm);
				return lista;
			} 
			error = Math.abs(b-a);
			//METODO QUE TABULA LOS DATOS DE LA ITERACION ACTUAL
			lista.add(new Iteracion(i,a,b,b-a,xm,fa,fxm,error));
			
			//CHECA SI EL ERROR ACTUAL ES MENOR AL ERROR PERMISIBLE PARA TERMINAR EL METODO
			if (error<errorPermisible){
				res.setText("Raiz: "+xm);
				return lista;
			}
			
			//COMPARA EL TEOREMA DE BOLZANO PARA SABER A QUE PUNTO SE VA IR EL VALOR DE Xi+1
			if (fxm*fa<0) {
				b = xm;
			} else {
				a = xm;
			}
			//AUMENTA LAS ITERACIONES
			i+=1;
			//SI LAS ITERACIONES SON MAYORES A 51 SE SALE;
		} while(i<51);
		
		res.setText("No se encontro en 50 iter.");
		
		return lista;
	}
	
	public  ObservableList<Iteracion> FP(double a,double b,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		
		double xm,error;
		int i=0;
		lista.add(new Iteracion(i++,a,f(a),0,0,0,0,0));
		do {
			//EVALUA EN LA FUNCION LOS PUNTOS
			double fa = f(a);
			double fb = f(b);
			
			//ENCUENTRA Xi+1
			xm = b - ((fb*(b-a))/(fb-fa));
			double fxm = f(xm);
			
			//TEOREMA DE BOLZANO PARA PUNTO MEDIO Y PUNTO 1
			if (fxm*fa==0){
				xm = ((fxm==0)?xm:a);
				//COMPARA SI BOLZANO DA 0 ENTONCES LA RAIZ ES EXACTA EN EL PUNTO MEDIO O EN EL PUNTO 1.
				res.setText("Raiz exacta: "+xm);
				return lista;
			} 

			error = Math.abs(b-xm);

			//METODO QUE TABULA LOS DATOS DE LA ITERACION ACTUAL
			lista.add(new Iteracion(i,b,fb,b-a,fb-fa,xm,error,fxm));
			
			//CHECA SI EL ERROR ACTUAL ES MENOR AL ERROR PERMISIBLE PARA TERMINAR EL METODO
			if (error<errorPermisible){
				res.setText("Raiz: "+xm);
				return lista;
			}
			//IGUALA Xi+1 AL PUNTO Xi
			b = xm;
			
			//AUMENTA LAS ITERACIONES
			i+=1;
			//SI LAS ITERACIONES SON MAYORES A 51 SE SALE;
		} while(i<51);
		res.setText("No se encontro en 50 iter.");
		return lista;
	}
	
	public ObservableList<Iteracion> SE(double a,double b,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		
		double xm,error;
		int i=1;
		
		do {
			//EVALUA LOS PUNTOS EN LA FUNCION
			double fa = f(a); //xi
			double fb = f(b); //xi+1
			
			//ENCUENTRA EL PUNTO Xi+2
			xm = b - ((fb*(b-a))/(fb-fa));
			
			error = Math.abs(xm-b);
		
			
			//EVALUA Xi+2
			double fxm = f(xm);
			//TABULA LOS VALORES DE LA ITERACION ACTUAL
			lista.add(new Iteracion(i,a,b,fa,fb,xm,error,fxm));
			
			//IGUALA EL PUNTO Xi+1 AL PUNTO Xi Y EL PUNTO Xi+2 AL PUNTO Xi+1
			a = b;
			b = xm;
			
			//CHECA SI EL ERROR ACTUAL ES MENOR AL ERROR PERMISIBLE PARA TERMINAR EL METODO
			if (error<errorPermisible){
				res.setText("Raiz: "+xm);
				return lista;
			}
			
			//AUMENTA LAS ITERACIONES
			i+=1;
			//SI LAS ITERACIONES SON MAYORES A 51 SE SALE;
		} while(i<51);
		res.setText("No se encontro en 50 iter.");
		return lista;
	}
		
	public ObservableList<Iteracion> NR(double a,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		double xm,error;
		int i=0;
		do {
			//EVALUA EL PUNTO EN LA FUNCION Y EN LA DERIVADA
			double fa = f(a); //xi
			double dfa = f(a,diff);
			//ENCUENTRA Xi+1
			xm = a - (fa/dfa);
			
			error = Math.abs(xm-a);
		
			//TABULA LOS VALORES DE LA ITERACION ACTUAL
			lista.add(new Iteracion(i,a,fa,dfa,xm,error));
			
			//EL PUNTO Xi ahora vale lo que el punto Xi+1
			a = xm;
			
			//COMPARA ERROR ACTUAL CON EL ERROR PERMISIBLE SI ES MENOR, SE TERMINA EL METODO
			if (error<errorPermisible){
				res.setText("Raiz: "+xm);
				return lista;
			}
	
			//AUMENTA LAS ITERACIONES
			i+=1;
			//SI LAS ITERACIONES SON MAYORES A 51 SE SALE;
		} while(i<51);
		res.setText("no se encontro una raiz sufcientemente aproximada en 50 iteraciones.");
		return lista;
	}
	
	public ObservableList<Iteracion> AS(double a,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		double xm,error=0,deltaError=0;
		int i=1;
		do {
			//ERROR ANTERIOR, ES 0 EN LA PRIMERA ITERACION Y EL ERROR ANTERIONER EN LAS SIGUIENTES
			deltaError = (i==1)?0:error;
			
			//EVALUA EL PUNTO EN LA FUNCION
			double fa = f(a); //xi
			
			//CALCULA EL PUNTO Xi+1
			xm = fa+a;

			error = Math.abs(xm-a);
		
			//TABULA CON LOS VALORES DE LA ITERACION ACTUAL
			lista.add(new Iteracion(i,a,xm,error,fa));
			
			//PUNTO Xi VALE LO QUE EL PUNTO Xi+1
			a = xm;
			
			//CHECA SI EL ERROR ACTUAL ES MENOR AL ERROR PERMISIBLE PARA TERMINAR EL METODO
			if (error<errorPermisible){
				res.setText("Raiz: "+xm);
				return lista;
			}
			//AUMENTA LAS ITERACIONES
			i+=1;
			//SI LAS ITERACIONES SON MAYORES A 50 SE TERMINA EL METODO
			if (i>50){
				break;
			}
		} while((i==2)?true:((error<deltaError)?true:false));
		res.setText("El Metodo Diverge.");
		return lista;
	}
	public ObservableList<Iteracion> N20(double a,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		double xm,error;
		int i=1;
		do {
			//EVALUA EL PUNTO EN LA FUNCION LA PRIMERA DERIVADA Y LA SEGUNDA DERIVADA
			double fa = f(a); //xi
			double dfa = f(a,diff);
			double df2a = f(a,diff2);
			
			//ENCUENTRA Xi+1
			xm = a-(fa/(dfa-(fa*df2a/(2*dfa))));
			
			error = Math.abs(xm-a);
			
			//IMPRIME LA ITERACION ACTUAL EN LA TABLA
			lista.add(new Iteracion(i,a,fa,dfa,df2a,xm,error));
			//Xi+1 ES GUARDADO EN Xi
			a = xm;

			//SI EL ERROR ACTUAL ES MENOR QUE EL ERROR PERMISIBLE SE TERMINA EL METODO.
			if (error<errorPermisible){
				res.setText("Raiz: "+xm);
				return lista;
			}
			//AUMENTA LAS ITERACIONES
			i+=1;
			//SI LAS ITERACIONES SON MAYORES A 50 SE TERMINA EL METODO
		} while(i<51);
		res.setText("no se encontro una raiz sufcientemente aproximada en 50 iteraciones.");
		return lista;
	}
	
	
	public double[][] Gauss(Label res){

			double[][] M = new double[Matriz.length][];
			
			//METODO QUE REGRESA LA MATRIZ TRIANGULAR SUPERIOR
			M = MTS(CopiarMatriz()); 
			
			ImprimirSolucion(DespejarVariables(M),res);
			return M;
			
			
		
	}

	public double[][] GaussJordan(Label res){

		double[][] M = new double[Matriz.length][];
		
		//METODO QUE REGRESA LA MATRIZ TRIANGULAR SUPERIOR
		M = MatrizIdentidad(CopiarMatriz()); 
		
		ImprimirSolucion(DespejarVariables(M),res);
		return M;
	}

	public double[] Cramer(Label res){

			double det = Determinant(Matriz);
			double[] dets = new double[Matriz.length+1];
			double[] solucion = new double[Matriz.length];
			dets[0] = det;
			//ALGORITMO DE CRAMER
			for (int x=0;x<Matriz.length;x++){
				double[][] M = new double[Matriz.length][];
				M = MTS(CopiarMatriz()); 
				for (int y=0;y<Matriz.length;y++){
					M[y][x] = M[y][Matriz.length];
				}
				dets[x+1] = Determinant(M);
				solucion[x] = dets[x+1]/det;
			}

			ImprimirSolucion(solucion,res);
			return  dets;

	}
	
	public double[][] Montante(Label res){

		double det = Determinant(Matriz);
		double[] solucion = new double[Matriz.length];
		double pivoteAnt = 1;
		double[][] M = new double[Matriz.length][];
		
		//ENCUENTRA LA MATRIZ TRANGULAR SUPERIOR
		M = MTS(CopiarMatriz()); 
		
		for (int k=0;k<M.length;k++){
			for  (int i=0;i<M.length;i++){
				if (i!=k){
					for (int j=M.length;j>=0;j--){
						M[i][j] = (M[k][k]*M[i][j]-M[i][k]*M[k][j])/pivoteAnt;
					}
				}
			}
			
			pivoteAnt = M[k][k];
		}
		
		det = M[0][0];
		
		//DIVIDE EL VECTOR b ENTRE EL DETERMINANTE PARA ENCONTRAR EL VECTOR SOLUCION
		for (int x=0;x<M.length;x++){
			solucion[x] = M[x][M.length]/det;
		}
		
		ImprimirSolucion(solucion,res);
		
		return M;

	}
	
	public ArrayList<double[]> Jacobi(Label res){
			double[][] M = new double[Matriz.length][];
			M =CopiarMatriz(); 
			//APROXIMACION INICIAL DE LA SOLUCION (VECTOR DE 0's)
			double[] solucionIni = new double[Matriz.length];
			Arrays.fill(solucionIni, 0);
			
			double[] solucion = new double[Matriz.length];
			ArrayList<double[]> iteracionesTabla = new ArrayList<double[]>();
			double error=0;
			boolean fin;
			int iteraciones=0;
			//ITERACIONES
			do{
				iteraciones++;
				fin = true;
				//ALGORITMO DE JACOBI
				for (int i=0;i<M.length;i++){
					solucion[i] = M[i][M.length]; 
					for (int j=0;j<M.length;j++){
						if (i!=j)
							solucion[i] -= M[i][j]*solucionIni[j];
					}
					solucion[i] = solucion[i] / M[i][i];
					error = Math.abs(solucionIni[i]-solucion[i]);
					
					//SI EL ERROR DE ALGUNA APROXIMACION ES MAYOR AL PERMISIBLE, LA VARIABLE FIN SE VUELVE FALSA Y EL METODO CONTINUA
					if (error > errorPermisible)
						fin = false;
					
				}
				
				//TABULA LOS VALORES DE LA ITERACION ACTUAL
				iteracionesTabla.add(concat(solucionIni,solucion));
				//GUARDA EL VALOR DE LA SOLUCION ENCONTRADA EN EL VECTOR SOLUCION INICIAL.
				for (int x=0;x<M.length;x++){
					solucionIni[x] = solucion[x];
				}
				//TERMINA SI LAS ITERACIONES SON MAYORES A 50 O FIN ES VERDADERO
			} while(fin == false && iteraciones<50);
			
			ImprimirSolucion(solucion,res);
			
			return iteracionesTabla;
	}
	
	public ArrayList<double[]> GaussSeidel(Label res){
			//ALGORITMO
			double[][] M = new double[Matriz.length][];
			M =CopiarMatriz(); 
			//APROXIMACION INICIAL DE LA SOLUCION (VECTOR DE 0's)
			double[] solucionIni = new double[Matriz.length];
			double[] solucion = new double[Matriz.length];
			Arrays.fill(solucion, 0);
			ArrayList<double[]> iteracionesTabla = new ArrayList<double[]>();
			double error=0;
			boolean fin;
			int iteraciones=0;
			
			//ITERACIONES
			do{
				iteraciones++;
				fin = true;
				
				//ALGORITMO GAUSS-SEIDEL
				for (int i=0;i<M.length;i++){
					double sol = M[i][M.length]; 
					for (int j=0;j<M.length;j++){
						if (i!=j)
							sol -= M[i][j]*solucion[j];
					}
					sol = sol / M[i][i];
					error = Math.abs(solucion[i]-sol);
					
					//SI EL ERROR DE ALGUNA APROXIMACION ES MAYOR AL PERMISIBLE, LA VARIABLE FIN SE VUELVE FALSA Y EL METODO CONTINUA
					if (error > errorPermisible)
						fin = false;
					//LA APROXIMACION ACTUAL DE LA VARIABLE SE GUARDA EN LA APROXIMACION INICIAL
					solucion[i] = sol;
				}
				
				//TABULA LOS VALORES DE LA ITERACION ACTUAL
				iteracionesTabla.add(concat(solucionIni,solucion));
				//GUARDA EL VALOR DE LA SOLUCION ENCONTRADA EN EL VECTOR SOLUCION INICIAL.
				for (int x=0;x<M.length;x++){
					solucionIni[x] = solucion[x];
				}
				//TERMINA SI LAS ITERACIONES SON MAYORES A 50 O FIN ES VERDADERO
			} while(fin == false && iteraciones<50);
			DecimalFormat df = new DecimalFormat("#.########");
			df.setRoundingMode(RoundingMode.HALF_UP);
			String str2="";
			for (int x=0;x<M.length;x++){
				str2+=String.format("X%s = %-12s \t",x+1,df.format(solucion[x]));
			}
			res.setText(str2);
			
			return iteracionesTabla;
	}

	public String IDF(VBox contenedor,double valorB,Label res){
		double[][] diferencias= new double[valoresx.length-1][valoresx.length-1]; 
		//Guarda la tabla de diferencias ne la matriz "diferencias" y regresa la potencia de la funcion.
		int i = TablaDiferencias(diferencias);
		
		ImprimirTablaDiferenciasG(diferencias,contenedor,i);
		
		//Encuentra el sistema de ecuaciones
		double[][] M = new double[i+1][i+2];
		for (int x=0;x<(i+1);x++){
			for (int y=0;y<(i+2);y++){
				if (y<i+1)
					M[x][y]=Math.pow(valoresx[x],i-y);
				else{
					M[x][y]=valoresy[x];
					//System.out.println(valoresy[x]);
				}
			}
		}
		ImprimirTitulo("Matriz aumentada del sistema",contenedor);
		ImprimirMatrizG(M,contenedor);
		//Encuentra la matriz triangular superior
		M = MTS(M); 
		
		//despeja la matriz triangular superior para encontrar el vector solucion
		double[] sol = DespejarVariables(M);
		
		ImprimirTitulo("solucion del sistema",contenedor);
		ImprimirLabel(ImprimirSolucion(sol),contenedor);
		//Imprime la funcion encontrada
		String strDisplay="";
		DecimalFormat df = new DecimalFormat("#.######");
		strDisplay = "";
		for (int x=0;x<sol.length-1;x++){	
			if (sol[x]!=0){
				strDisplay+= ((sol[x]!=1)?df.format(sol[x]):"")+"x"+((i-x>1)?("^"+df.format(i-x)):"");
			}
			strDisplay+=((sol[x+1]>0)?" +":((sol[x+1])==0)?"":" ");
		}
		strDisplay+=(sol[sol.length-1]!=0)?df.format(sol[sol.length-1]):"";
		
		P.parseExpression(strDisplay);
		res.setText("f("+df.format(valorB)+")="+df.format(f(valorB)));
		
		ImprimirTitulo("Funcion encontrada",contenedor);
		ImprimirLabel(strDisplay,contenedor);
		
		return (strDisplay);
	}

	public String IN(double xBuscado,VBox contenedor){

		
		double valorBuscado = (xBuscado-valoresx[0])/(valoresx[1]-valoresx[0]);
		
		double[][] diferencias= new double[valoresx.length-1][valoresx.length-1]; 
		//Guarda la tabla de diferencias ne la matriz "diferencias" y regresa la potencia de la funcion.
		int i = TablaDiferencias(diferencias);
		
		ImprimirTablaDiferenciasG(diferencias,contenedor,i);
		
		//Calcula el resultado usando la formula de newton
		double res = valoresy[0];
		
		String sustK = "((x - "+(valoresx[0])+")/ ("+(valoresx[1]-valoresx[0])+"))";
		String polinomio = "Yk = "+valoresy[0]+"";
		String funcion = +valoresy[0]+"";
		
		for (int x=0;x<i;x++){
			if (diferencias[0][x]!=0){
			double sumaK = 1;
			polinomio +=(" + "+(diferencias[0][x])+"k");
			funcion +=(" + "+(diferencias[0][x])+sustK);
			for (int y=0;y<x+1;y++){
				sumaK *= valorBuscado-y;
				if (y>0){
					funcion += "("+sustK+"-"+(y)+")";
					polinomio += "(k-"+(y)+")";
				}
			}
			double fac = factorial(x+1);
			res += diferencias[0][x]*sumaK/fac;
			polinomio += ("/"+(x+1)+"!");
			funcion +=  ("/("+fac+")");
			}
		}
		ImprimirTitulo("Polinomio de Newton",contenedor);
		ImprimirLabel(polinomio,contenedor);
		
		System.out.println(funcion);
			try {
	            P.parseExpression(funcion);
			    System.out.println("f("+xBuscado+")="+f(xBuscado));
		}  catch (Exception e) {
			
		}

		ImprimirTitulo("Funcion Encontrada",contenedor);
		ImprimirLabel(IDFnoG(xBuscado),contenedor);
			
			
		DecimalFormat df = new DecimalFormat("#.######");
		return("f("+df.format(xBuscado)+") = "+df.format(res));
	}
	
	public String IL(double x,VBox contenedor){
		//LECTURA E INICIALIZACION DE VALORES
		double suma =0;
		DecimalFormat df = new DecimalFormat("#.######");
		ImprimirTitulo("Valores",contenedor);
		double[][] M = new double[valoresx.length][2];
		for (int i=0;i<valoresx.length;i++){
			M[i][0] = valoresx[i];
			M[i][1] = valoresy[i];
		}
		String[] encabezados = {"X","Y"};
 		ImprimirEncabezado(encabezados,contenedor);
		ImprimirMatrizG(M,contenedor);
		//System.out.print("Y = ");	
		//ALGORITMO DE LAGRANGE
		ImprimirTitulo("LaGrange Operaciones",contenedor);
		for (int i=0;i<valoresx.length;i++){
			double mult=valoresy[i];

				//PROCEDIMIENTO
			String numerador = "("+df.format(valoresy[i])+")";
			String denominador = "";
			double div = 1; 
			
			for (int j=0;j<valoresx.length;j++){
				if (j!=i){
					
					numerador += "("+df.format(x)+"-"+df.format(valoresx[j])+")";					//PROCEDIMIENTO
					denominador += "("+df.format(valoresx[i])+"-"+df.format(valoresx[j])+")";		 //PROCEDIMIENTO
					mult = mult*(x-valoresx[j]);
					div = div*(valoresx[i]-valoresx[j]);
				}
			}
			ImprimirLabel(("("+numerador+"  /  "+denominador+") + "),contenedor);
			suma = suma + (mult/div);
		}
		//IMPRIME RESULTADO
		
		return ("f("+df.format(x)+") = "+df.format(suma));
	}
	
	public String IMC(int m,double valorB,VBox contenedor,Label res){
		int n = m+1;
		double[] a = new double[(m*2)+1];
		double[] b = new double[m+1];
		double[][] M= new double[m+1][m+2];
		double[][] tabla = new double[valoresx.length+1][m*2+n];
		
		ImprimirTitulo("Tabla de sumatorias",contenedor);
		String[] encabezados = new String[m*2+n];
		encabezados[0] = "X";
		encabezados[1] = "Y";
		for (int i=2;i<encabezados.length;i++){
			if (i<m*2+1)
				encabezados[i] = "X^"+i;
			else
				encabezados[i] = "Y*X^"+(i-m*2);
		}
		ImprimirEncabezado(encabezados,contenedor);
		
		for (int i=0;i<tabla.length-1;i++){
			tabla[valoresx.length][0] += tabla[i][0] = valoresx[i];
			tabla[valoresx.length][1] +=tabla[i][1] = valoresy[i];
			for(int j=2;j<m*2+1;j++){
				tabla[valoresx.length][j] += tabla[i][j] = Math.pow(valoresx[i],j);
			}
			for(int j=m*2+1;j<tabla[i].length;j++){
				tabla[valoresx.length][j] += tabla[i][j] = valoresy[i]*Math.pow(valoresx[i],j-n-1);
			}
		}
		ImprimirTablaSum(tabla,contenedor);
		//ENCUENTRA LAS SUMATORIAS DE LA TABLA
		a[0]=valoresx.length;
		for (int x=0;x<valoresx.length;x++){
			b[0] += valoresy[x];
			for (int y=1;y<a.length;y++){
				a[y] += Math.pow(valoresx[x],y);
			}
			for (int y=1;y<b.length;y++){
				b[y] += valoresy[x]*Math.pow(valoresx[x],y);
			}
		}
		//ENCUENTRA LA MATRIZ QUE REPRESENTA EL SISTEMA DE ECUACIONES
		for (int x=0;x<m+1;x++){
			for (int y=0;y<m+1;y++){
				M[x][y] = a[x+y];
			}
			M[x][m+1] = b[x];
		}
		
		ImprimirTitulo("Matriz aumentada del sistema",contenedor);
		ImprimirMatrizG(M,contenedor);
		//LLAMA A GAUSS PARA LA MATRIZ Y REGRESA UNA MATRIZ TRIANGULAR SUPERIOR;
		
		M = MatrizIdentidad(M);
		//PASA LA SOLUCIOND DE LA MATRIZ TRIANGULAR SUPERIOR A UN VECTOR SOLUCION
		double[] sol = DespejarVariables(M);
		ImprimirTitulo("Solucion del sistema",contenedor);
		ImprimirLabel(ImprimirSolucion(sol),contenedor);
		//IMPRINME SOLUCION
		String strDisplay="";
		DecimalFormat df = new DecimalFormat("#.######");
		 strDisplay+=("");
		for (int x=sol.length-1;x>0;x--){	
			if (sol[x]!=0){
				 strDisplay+=( ((sol[x]!=1)?df.format(sol[x]):"")+"x"+((x>1)?("^"+df.format(x)):"")+((x-1>=0)?((sol[x-1]>0)?" +":" "):" "));
			}
		}
		strDisplay+=((sol[0]!=0)?df.format(sol[0]):"");
		ImprimirTitulo("Funcion",contenedor);
		ImprimirLabel("f(x)="+strDisplay,contenedor);
		P.parseExpression(strDisplay);
		P.addVariable("x", valorB);
		res.setText("f("+df.format(valorB)+")="+df.format(P.getValue()));
		return(strDisplay);
	}
	
	public ObservableList<Iteracion> DDF(double x,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		int n = 1;
		double da = 100;
		int k = 1;
		double error,d;
		//System.out.println(String.format("\nDiferenciacion por Limites\n%s\t %-15s %-15s %-15s %-15s %-15s %-15s %-15s","Iteracion","h","k","x+h","f(x+h)","f(x)","f(x+h)-f(x)","error"));
		do {
			k *= 10;
			double h = (1/(double)k);
			double fxh=f(x+h);
			double fx = f(x);
			d = (fxh-f(x))*k;
			error = Math.abs(da-d);
			lista.add(new Iteracion(n,h,k,x+h,fxh,fx,fxh-fx,error));
			da = d;
			n++;
			
		} while(error>errorPermisible);
		res.setText("f'("+x+")="+d);
		
		return lista;
	}

	public ObservableList<Iteracion> IT(double a, double b, int n,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		double x=a;
		double[] y = new double[n];
		double h = (b-a)/(n-1);
		//System.out.println(String.format("\nIntegracion por Trapecio (Formula)\n%s\t %-15s %-15s %-15s %-15s","Iteracion","x","y","m","m*y"));
		for (int i=0;i<n;i++){
			y[i] = f(x);
			lista.add(new Iteracion(i+1,x,y[i],(i==0||i==n-1)?1:2,((i==0||i==n-1)?1:2)*y[i]));
			x+=h;
		}
		double area = y[0]+y[n-1];
		for (int i=1;i<n-1;i++){
			area += 2*y[i];
		}
		res.setText("Area="+(area*(h/2)));
		return lista;
	}
	
	public ObservableList<Iteracion>  ITtabla(double h,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		int n = valoresy2.length;
		double area = 0;
		for (int i=0;i<n;i++){
			double m  = (i==0||i==n-1)?1:2;
			area += m*valoresy2[i];
			lista.add(new Iteracion(i+1,valoresy2[i],m,m*valoresy2[i]));
		}
		res.setText("Area="+(area*(h/2)));
		return lista;
	}
	
	public ObservableList<Iteracion> IS(double a, double b, int n,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		double x=a;
		double h = (b-a)/(n-1);
		double area = 0;
		//System.out.println(String.format("\nIntegracion por Trapecio (Formula)\n%s\t %-15s %-15s %-15s %-15s","Iteracion","x","y","m","m*y"));
		for (int i=0;i<n;i++){
			double m = (i==0||i==n-1)?1:((i%2==0)?2:4);
			double fx = f(x);
			double mfx =m*fx;
			area +=mfx;
			lista.add(new Iteracion(i+1,x,fx,m,mfx));
			x+=h;

		}
		res.setText("Area="+(area*(h/3)));
		return lista;
	}

	public ObservableList<Iteracion> IStabla(double h,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		double area = 0;
		for (int i=0;i<valoresy2.length;i++){
			double m = (i==0||i==valoresy2.length-1)?1:((i%2==0)?2:4);
			area += m*valoresy2[i];
			lista.add(new Iteracion(i+1,valoresy2[i],m,m*valoresy2[i]));
		}
		res.setText("Area="+(area*(h/3)));
		return lista;
	}
	
	public ObservableList<Iteracion> ME(double x,double y,double n,double h,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		double sol = 0;
		lista.add(new Iteracion(0,x,y));
		for (int i=0;i<n;i++){
			y = y+h*f(x,y);
			x+=h;		
			//TABULA LOS VALORES DE LA ITERACION ACTUAL
			lista.add(new Iteracion(i+1,x,y));
			sol = y;
		}
		res.setText("Solucion: "+sol);
		return lista;		
	}

	public ObservableList<Iteracion> MEG(double x,double y,double n,double h,Label res){
		ObservableList<Iteracion> lista = FXCollections.observableArrayList();
		double sol = 0;
		lista.add(new Iteracion(0,x,y));
		for (int i=0;i<n;i++){
			double yMod = y+h*f(x,y);
			y = y+(h*((f(x,y)+f(x+h,yMod))/2));
			x+=h;		
			//TABULA LOS VALORES DE LA ITERACION ACTUAL
			lista.add(new Iteracion(i+1,x,y));
			sol = y;
		}
		res.setText("Solucion: "+sol);
		return lista;		
	}
	
	public String IDFnoG(double valorB){
		double[][] diferencias= new double[valoresx.length-1][valoresx.length-1]; 
		//Guarda la tabla de diferencias ne la matriz "diferencias" y regresa la potencia de la funcion.
		int i = TablaDiferencias(diferencias);

		
		//Encuentra el sistema de ecuaciones
		double[][] M = new double[i+1][i+2];
		for (int x=0;x<(i+1);x++){
			for (int y=0;y<(i+2);y++){
				if (y<i+1)
					M[x][y]=Math.pow(valoresx[x],i-y);
				else{
					M[x][y]=valoresy[x];
					//System.out.println(valoresy[x]);
				}
			}
		}
		//Encuentra la matriz triangular superior
		M = MTS(M); 
		
		//despeja la matriz triangular superior para encontrar el vector solucion
		double[] sol = DespejarVariables(M);
		//Imprime la funcion encontrada
		String strDisplay="";
		DecimalFormat df = new DecimalFormat("#.######");
		strDisplay = "";
		for (int x=0;x<sol.length-1;x++){	
			if (sol[x]!=0){
				strDisplay+= ((sol[x]!=1)?df.format(sol[x]):"")+"x"+((i-x>1)?("^"+df.format(i-x)):"");
			}
			strDisplay+=((sol[x+1]>0)?" +":((sol[x+1])==0)?"":" ");
		}
		strDisplay+=(sol[sol.length-1]!=0)?df.format(sol[sol.length-1]):"";
		
		return (strDisplay);
	}
	
	public int TablaDiferencias(double[][] diferencias){	
		int potencia = 0;
		Boolean fin = false;
		//double[] diferencias= new double[valoresy.length-1];
		//System.arraycopy(valoresy, 0, diferencias, 0, valoresy.length);
		//double[][] diferencias = new double[valoresx.length-1][valoresx.length-1];
		
		//Encuentra el grado del polinomio
		for (int i=0;i<valoresx.length-1;i++){
			fin = true;
			for (int j=1;j<valoresx.length-i;j++){
				diferencias[j-1][i] = (i==0)?(valoresy[j]-valoresy[j-1]):(diferencias[j][i-1] - diferencias[j-1][i-1]);
				if (j>1 && diferencias[j-2][i] != diferencias[j-1][i]){
					fin = false;
					//potencia = Math.max(potencia, j);
				}
			}
			if (fin==true){
				potencia = i+1;
				break;
			}
		}
		
		
		return potencia;
	}
	
	public double[][] CopiarMatriz(){
		double[][] M = new double[Matriz.length][];
		for (int i = 0;i<Matriz.length;i++){
			M[i] = new double[Matriz.length+1];
			System.arraycopy(Matriz[i], 0, M[i], 0, Matriz[i].length);
		}
		return M;
	}
	public double[] DespejarVariables(double[][] M){
		int m = M.length;
		double[] solucion = new double[m];
		solucion[m-1] = (M[m-1][m]/M[m-1][m-1]);
		for (int k=m-1;k>=0;k--){
			double suma = 0;
			for (int j=m-1;j>k;j--){
				suma += M[k][j]*solucion[j];			
			}
			solucion[k]=round(((-suma+M[k][m])/(M[k][k])),5);
		}
		
		return solucion;
	}
	public String ImprimirSolucion(double[] solucion){
		DecimalFormat df = new DecimalFormat("#.########");
		df.setRoundingMode(RoundingMode.HALF_UP);
		String str = "";
		for (int x=0;x<solucion.length;x++){
			str += String.format("X%s = %-10s \t",(x+1),df.format(solucion[x]));
		}
		return str;
	}
	public void ImprimirSolucion(double[] solucion,Label res){
		DecimalFormat df = new DecimalFormat("#.########");
		df.setRoundingMode(RoundingMode.HALF_UP);
		String str = "";
		for (int x=0;x<solucion.length;x++){
			str += String.format("X%s = %-10s \t",(x+1),df.format(solucion[x]));
		}
		res.setText(str);
	}
	
	public double[][] MTS(double[][] M){ //Transforma a Matriz triangular superior 
		
		for (int k=0;k<M.length-1;k++){
			for (int i=k+1;i<M.length;i++){
				if (M[k][k]==0)
					CambioRenglon(M,k);
				double factor = M[i][k]/M[k][k]; 
				for(int j=k;j<M.length+1;j++){
					M[i][j] =  M[i][j]-(M[k][j]*factor);
				}
				//ImprimirMatriz(M);           			Debug	
				//System.out.println();
			}
		}
		
		return M;
	}
	public void CambioRenglon(double[][] M,int i){
		double	temp=0;
		for (int x=i+1;x<M.length;x++){
			if (M[x][i]!=0){
				for (int y=0;y<M.length+1;y++){
					temp = M[i][y];
					M[i][y] = M[x][y];
					M[x][y] = temp;
				}
				x=M.length;
			}
		}
	}
	
	public double round(double a,int b){
		double n = Math.pow(10,b);
		return Math.round(a*n)/n;
	}
	
	public double[][] MatrizIdentidad(double[][] M){ // Transforma a matriz Identidad;
		for (int i=0;i<M.length;i++){
			for (int k=M.length;k>=0;k--){
				if (M[i][i]==0)
					CambioRenglon(M,i);
				M[i][k] = M[i][k]/M[i][i];
			}
			for (int j=0;j<M.length;j++){
				//double factor = M[i][j]/M[j][j];
				for (int k=M.length;k>=0;k--){
					if (j!=i){
						M[j][k] -= M[j][i]*M[i][k];
					}
				}
				//ImprimirMatriz(M);				Procedimiento
				//System.out.println();
			}
		}
		
		return M;
	}
	
	
	public Boolean validarFun(String str){
		P.parseExpression(str);
		P.addVariable("x", 0);
		P.removeVariable("y");
		if (!Double.isNaN(P.getValue())){
			ultimaFunc = str;
			return true;
		} else {
			return false;
		}
	}
	public Boolean validarFunY(String str){
		P.parseExpression(str);
		P.addVariable("x", 0);
		P.addVariable("y", 0);
		if (!Double.isNaN(P.getValue())){
			ultimaFuncY = str;
			return true;
		} else {
			return false;
		}
	}
	public double f(double val){
		return f(val,P);

	}
	public double f(double val,double val2){
		P.addVariable("x", val);
		P.addVariable("y", val2);
		return P.getValue();
	}
	public double f(double val,DJep func){
		func.addVariable("x", val);
		//func.removeVariable("y");
		return func.getValue();
	}

	public DJep Derivar(DJep func,Node node){
		while (true){
			try {	
				Node n = P.differentiate(node, "x");
				DJep temp = InitParser();
				temp.parseExpression(temp.toString(n));
				return temp;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	public DJep InitParser(){
		DJep Q = new DJep();
		Q.addStandardConstants();
		Q.addStandardFunctions();
		Q.addVariable("x", 0);
		Q.addVariable("y", 0);
		Q.setImplicitMul(true);
		Q.addComplex();
		Q.getAllowUndeclared();
		Q.getAllowAssignment();
		Q.addStandardDiffRules();
	
		return Q;
	}
	
	public void TeclaParaContinuar()
	 { 
	        System.out.println("\nPresiona enter para continuar...");
	        try
	        {
	            System.in.read();
	        }  
	        catch(Exception e)
	        {}  
	 }

	
	public Boolean bolzano(double a,double b){
		Boolean res = false;
		if (f(a)*f(b)<=0)
			res = true;
		return res;
	}
	
	public double Determinant(double[][] matrix){ //method sig. takes a matrix (two dimensional array), returns determinant.
	    double sum=0; 
	    double s;
	    if(matrix.length==1){  //bottom case of recursion. size 1 matrix determinant is itself.
	      return(matrix[0][0]);
	    }
	    for(int i=0;i<matrix.length;i++){ //finds determinant using row-by-row expansion
	      double[][]smaller= new double[matrix.length-1][matrix.length-1]; //creates smaller matrix- values not in same row, column
	      for(int a=1;a<matrix.length;a++){
	        for(int b=0;b<matrix.length;b++){
	          if(b<i){
	            smaller[a-1][b]=matrix[a][b];
	          }
	          else if(b>i){
	            smaller[a-1][b-1]=matrix[a][b];
	          }
	        }
	      }
	      if(i%2==0){ //sign changes based on i
	        s=1;
	      }
	      else{
	        s=-1;
	      }
	      sum+=s*matrix[0][i]*(Determinant(smaller)); 
	    }
	    return(sum); //returns determinant value. once stack is finished, returns final determinant.
	  }
	public double factorial(double n){
		double sol = 1;
		for (int x=1;x<=n;x++){
			sol *= x;
		}
		return sol;
	}
	public Boolean TDD(){
		Boolean diagonalDominante = true;
		for (int x=0;x<Matriz.length;x++){
			double suma = 0;
			for(int y=0;y<Matriz.length;y++){
				if (y!=x)
					suma += Matriz[x][y];
			}
			diagonalDominante = (Math.abs(Matriz[x][x])<=Math.abs(suma))?false:diagonalDominante;
			
		}
		return diagonalDominante;
	}
	public double[] concat(double[] a, double[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   double[] c= new double[aLen+bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
		}
	public void ImprimirTablaDiferenciasG(double[][] M,VBox contenedor,int grado){
		String[][] completa = new String[valoresx.length][grado+2];
		for (int i=0;i<valoresx.length;i++){
			completa[i][0] = valoresx[i]+"";
			completa[i][1] = valoresy[i]+"";
			int limite = Math.min(grado+2, valoresy.length-i+1);
			for (int j=2;j<completa[i].length;j++){
				if (j<limite)
					completa[i][j]=M[i][j-2]+"";
				else
					completa[i][j] = "";	
			}
		}
		String[] titulos = new String[grado+2];
		titulos[0] = "X";
		titulos[1] = "Y";
		for (int i=2;i<titulos.length;i++){
			titulos[i] = "d"+(i-1)+"Y";
		}
		ImprimirTitulo("Tabla de diferencias",contenedor);
		ImprimirEncabezado(titulos,contenedor);
		ImprimirMatrizG(completa,contenedor);
	}
	public void ImprimirLabel(String titulo,VBox contenedor){
		Label title = new Label(titulo);
		 contenedor.getChildren().add(title);
		 title.getStyleClass().add("input-title");	
		 Label space = new Label();
		 space.setPrefHeight(20);
		 contenedor.getChildren().add(space);
	}
	public void ImprimirTitulo(String titulo,VBox contenedor){
		Label title = new Label(titulo);
		 contenedor.getChildren().add(title);
		 title.getStyleClass().add("input-title");	
		 title.getStyleClass().add("larger-title");
		 Label space = new Label();
		 space.setPrefHeight(20);
		 contenedor.getChildren().add(space);
	}
	public void ImprimirEncabezado(String[] titulos,VBox contenedor){
		
		HBox tempHbox = new HBox();
		tempHbox.getStyleClass().add("matrix-row");
		contenedor.getChildren().add( tempHbox);
		for (int j=0;j<titulos.length;j++){
			 Label tempLabel = new Label(titulos[j]);
			 tempLabel.getStyleClass().add("output-matriz-label");
			 tempLabel.setPrefWidth(125);
			 tempHbox.getChildren().add(tempLabel);
		 }
	}
	public void ImprimirMatrizG(String[][] M,VBox contenedor){
		 for (int i=0;i<M.length;i++){
			 HBox tempHbox = new HBox();
			 tempHbox.getStyleClass().add("matrix-row");
			 contenedor.getChildren().add( tempHbox);
			 for (int j=0;j<M[i].length;j++){
				 Label tempLabel = new Label(M[i][j]+"");
				 tempLabel.getStyleClass().add("output-matriz-label-med");
				 tempLabel.setPrefWidth(125);
				 tempHbox.getChildren().add(tempLabel);
			 }
			 
		 }
	}
	public void ImprimirMatrizG(double[][] M,VBox contenedor){
		 for (int i=0;i<M.length;i++){
			 HBox tempHbox = new HBox();
			 tempHbox.getStyleClass().add("matrix-row");
			 contenedor.getChildren().add( tempHbox);
			 for (int j=0;j<M[i].length;j++){
				 Label tempLabel = new Label(M[i][j]+"");
				 tempLabel.getStyleClass().add("output-matriz-label-med");
				 tempLabel.setPrefWidth(125);
				 tempHbox.getChildren().add(tempLabel);
			 }
			 
		 }
	}
	public void ImprimirTablaSum(double[][] M,VBox contenedor){
		 for (int i=0;i<M.length;i++){
			 HBox tempHbox = new HBox();
			 tempHbox.getStyleClass().add("matrix-row");
			 contenedor.getChildren().add( tempHbox);
			 for (int j=0;j<M[i].length;j++){
				 Label tempLabel = new Label(M[i][j]+"");
				 tempLabel.getStyleClass().add("output-matriz-label-med"+((i==M.length-1)?"-bold":""));
				 tempLabel.setPrefWidth(125);
				 tempHbox.getChildren().add(tempLabel);
			 }
			 
		 }
	}
}
