package menu.youtube.ejemplo.hola_mundo.ariel.alex.programapostfijo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public TextView textView;
    public TextView textViewValor;
    public TextView textViewOP;
    public TextView textViewEV;
    public EditText editText1;
    public Button boton;
    public CheckBox checkBox;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        textViewValor = (TextView)findViewById(R.id.textViewValor);
        editText1 = (EditText) findViewById(R.id.editText1);
        textViewOP = (TextView) findViewById(R.id.textViewOP);
        textViewEV = (TextView) findViewById(R.id.textViewEV);
        boton = (Button)findViewById(R.id.button);
        checkBox = (CheckBox)findViewById(R.id.checkBox);

        boton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        try {
            ImplementacionPostfijoEvalucion IPE = new ImplementacionPostfijoEvalucion();
//	posibles ejemplos
//		String exp = "((a/(b+c*d))/((b*c)/(a+d*b-c)))+b-d";
//		String exp = "((2/(1+3*2))/((1*3)/(2+2*1-3)))+1-2";
//		String exp = "123+4*1.8-56.321/2.5";
//		String exp = "((123/(15.15+1.123*4))/((15.15*1.123)/(123+1.123*15.15-1.123)))+15.15-4";
//		String exp = "((4-1)*5)+(64/(2^5))";
            //f=(a*b+(d+a)/(b-c))/((c-a)/(c*b+d))
            //String exp = "((b+((b^2-a*c)^(1/2)))/(d*a))/((b+a)/(c+b*d))";}
            String exp = editText1.getText().toString();
            //String exp = "((3.0+((3.0^2-1.6*5)^(1/2)))/(15.15*1.6))/((3.0+5)/(5+3.0*15.15))";
            Cola cola1 = IPE.aCola(exp);
            Cola cola2 = IPE.aCola(exp);
            cola1 = IPE.aPostfijo(cola1);
            IPE.StrPilaOP = "";
            cola2 = IPE.aPostfijo(cola2);
            /**TENER EN CUENTA*/
            textViewOP.setText(IPE.StrPilaOP);

//        System.out.print ("PostFijo: ");
            String resul = "";

            String t = "";
            if(!checkBox.isChecked())
                t = " ";

            while(!cola1.vacio()){
                resul += cola1.sacar();
                resul += t;
            }

            textViewValor.setText(resul);

            try{
                textView.setText(("Valor: "+String.format("Evaluacion: %.5f\n",IPE.eValuar(cola2))).toString());
                /**TENER EN CUENTA*/
                if(!checkBox.isChecked())
                    textViewEV.setText(IPE.StrPilaEV);
                else
                    textViewEV.setText("");

            }catch(NumberFormatException e){
                textView.setText("Valor: Literal...");
            }
            Toast.makeText(this,"Dev: Alex Ariel. C",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(this,"Entrada No Valida..",Toast.LENGTH_SHORT).show();
            textViewValor.setText("");
            textView.setText("");
        }

    }

}

//Aplicacion: Pilas,Colas,Notacion Post Fijo y Evalucion

class ImplementacionPostfijoEvalucion{
    /*BORRAR LUEGO*/
    public String StrPilaOP = "",StrPilaEV = "";

    public Cola aCola(String cad){
        Cola c = new Cola(cad.length()+1);
        String aux = "";
        for(int i=0;i<cad.length();i++){
            if(Character.isDigit(cad.charAt(i))||cad.charAt(i)=='.'){
                aux+=cad.charAt(i);
                if(i==cad.length()-1)
                    c.insertar(aux);
            }else{
                if(aux!="")
                    c.insertar(aux);
                c.insertar(String.valueOf(cad.charAt(i)));
                aux="";
            }
        }
        return c;
    }

    public int prioriadad(String o){
        switch (o) {
            case "+":
            case "-": return 1;
            case "*":
            case "/": return 2;
            case "^": return 3;
            default : return 0;
        }
    }

    public Cola aPostfijo(Cola colainf){
        Cola cola = new Cola(colainf.tamaño()+1);
        Pila pila = new Pila(colainf.tamaño()+1);
        String x;
        /*TENER UN CUENTA*/

        while(!colainf.vacio()){
            x = colainf.sacar();
            switch (x) {
                case "(":
                    pila.push(x);
                    /*AQUI PARA PILAEV*/
                    StrPilaOP = "\n" + x + StrPilaOP;
                    break;
                case ")":
                    while(!pila.EnTope().equals("("))
                        cola.insertar(pila.pop());
                    pila.pop();
                    break;
                case "^":
                case "/":
                case "*":
                case "-":
                case "+":
                    while(!pila.vacio()&&prioriadad(x)<=prioriadad(pila.EnTope()))
                        cola.insertar(pila.pop());

                    pila.push(x);
                    /*AQUI PARA PILAOP*/
                    StrPilaOP = "\n" + x + StrPilaOP;
                    break;
                default : cola.insertar(x); break;
            }
        }
        while(!pila.vacio())
            cola.insertar(pila.pop());
        return cola;
    }

    public double eValuar(Cola colapostf){
        Pila pila = new Pila(colapostf.tamaño()+1);
        double x,y,z;
        x = y = z = 0;
        String aux;
        while(!colapostf.vacio()){
            aux = colapostf.sacar();
            switch (aux) {
                case "^":
                case "/":
                case "*":
                case "-":
                case "+":
                    y = Double.parseDouble(pila.pop());
                    x = Double.parseDouble(pila.pop());

                    switch (aux) {
                        case "^":
                            z = Math.pow(x,y);
                            break;
                        case "/":
                            z = x/y;
                            break;
                        case "*":
                            z = x*y;
                            break;
                        case "-":
                            z = x-y;
                            break;
                        case "+":
                            z = x+y;
                            break;
                    }
                    pila.push(String.valueOf(z));
                    /*AQUI PARA PILAEV*/
                    StrPilaEV = "\n" + String.valueOf(z)+StrPilaEV ;

                    break;
                default : pila.push(aux);
                    /*AQUI PARA PILAEV*/
                    StrPilaEV = "\n" + aux +StrPilaEV ;

            }

        }
        return Double.parseDouble(pila.pop());
    }

}


class Pila{
    private int tope;
    private String p[];

    public Pila(int n){
        p = new String[n];
        tope = -1;
    }

    public boolean vacio(){
        return tope == -1;
    }

    public String EnTope(){
        return p[tope];
    }

    public void push(String dato){
        p[++tope]=dato;
    }

    public String pop(){
        return p[tope--];
    }

    public void Elimina(){
        while(!vacio())
            pop();
    }
}

class Cola{
    private int primero;
    private int ultimo;
    private String C[];

    public Cola(int n){
        primero = -1;
        ultimo = primero;
        C = new String[n];
    }

    public boolean vacio(){
        return primero == -1;
    }

    public String primero(){
        return C[primero];
    }

    public String ultimo(){
        return C[ultimo];
    }

    public void insertar(String d){
        if(vacio())
            primero = 0;
        C[++ultimo]=d;
    }

    public String sacar(){
        String dato = C[primero];
        if(primero==ultimo)
            primero = ultimo = -1;
        else
            primero++;
        return dato;
    }

    public void vaciar(){
        while(!vacio())
            sacar();
    }

    public int tamaño(){
        return C.length;
    }

}

