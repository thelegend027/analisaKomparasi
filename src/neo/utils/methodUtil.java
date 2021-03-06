/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neo.utils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import neo.table.Dataset;
import neo.table.deskriptif;
import neo.table.naiveBayesPobabilitas;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author SEED
 */
public class methodUtil {
     
    
    static deskriptif avaragehoursSTD;
    static deskriptif timespendcompanySTD;
    static deskriptif numberprojectSTD;
    
    static deskriptif satisfactionSTD;
    static deskriptif evaluationSTD;
    
    

    public static List<Dataset> NBclasificationAll(
            List<naiveBayesPobabilitas> NBtrain,
            List<Dataset> NBdataLatih,
            List<Dataset> NBdataUji)
    {
        
        
        avaragehoursSTD = new deskriptif();
        timespendcompanySTD = new deskriptif();
        numberprojectSTD = new deskriptif();
        
        satisfactionSTD = new deskriptif();
        evaluationSTD = new deskriptif();
        
        //Data statistik data latih
        NBdataLatih.stream().forEachOrdered((d) -> {
            avaragehoursSTD.addValue(d.getAvaragehours());
            timespendcompanySTD.addValue(d.getTimespendcompany());
            numberprojectSTD.addValue(d.getNumberproject());
            satisfactionSTD.addValue(d.getSatisfaction());
            evaluationSTD.addValue(d.getEvaluation());
        });        

        List<String> meta = new LinkedList<>();
        meta.add("workAccident;");
        meta.add("promotion;");
        meta.add("Division;");
        meta.add("Salary;");
        
        meta.add("avaragehours;");
        meta.add("timespendcompany;");
        meta.add("numberproject;");
        meta.add("evaluation;");
        meta.add("satisfaction;");
        
        Map<String,deskriptif> meanMap = new LinkedHashMap<>();
        meanMap.put("evaluation;", evaluationSTD);
        meanMap.put("satisfaction;", satisfactionSTD);
        meanMap.put("numberproject;", numberprojectSTD);
        meanMap.put("timespendcompany;", timespendcompanySTD);
        meanMap.put("avaragehours;", avaragehoursSTD);
        
        
        Map<String, Double> collectLeft = NBtrain
                .stream()
                .collect(
                        Collectors.groupingBy((naiveBayesPobabilitas e)-> {return e.getAtribut();}, 
                        Collectors.summingDouble((naiveBayesPobabilitas a) -> a.getProbabilitasLeft())
                ));
//        System.out.println("collectLeft = " + collectLeft);
        Map<String, Double> collecNotLeft = NBtrain
                .stream()
                .collect(
                        Collectors.groupingBy((naiveBayesPobabilitas e)-> {return e.getAtribut();}, 
                        Collectors.summingDouble((naiveBayesPobabilitas a) -> a.getProbabilitasNotLeft())
                ));        
        double pLeft;
        double PNotLeft;

        for (Dataset X : NBdataUji) {            
            pLeft = 1d;
            PNotLeft = 1d;
            for (String string : meta) {
                String key = string;
                Object m = X.getMeta(string);
                if (m.getClass() == Integer.class) {
                    Integer temp = (Integer) m;
                    key += temp == 1 ? "True":"False";
                }
                else if (m.getClass() == String.class) {
                    String temp = (String) m;
                    key += temp;
                }
                else if (m.getClass() == Double.class) {
                    Double temp = (Double) m;
                    key += temp > meanMap.get(key).getMean() ? "True":"False"; 
                }
                if (collectLeft.get(key) != null) {
                    pLeft = pLeft * collectLeft.get(key);
                    PNotLeft = PNotLeft * collecNotLeft.get(key);
                }   else {
                    pLeft = pLeft * collectLeft.get("Division;null");
                    PNotLeft = PNotLeft * collecNotLeft.get("Division;null");                
                }
            }
            X.setKelas(pLeft > PNotLeft ? 1d:0d);
        }
        return NBdataUji;
    }
    public static List<naiveBayesPobabilitas> NBtraining(List<Dataset> dataLatih,int k)
    {        
        List<naiveBayesPobabilitas> tempTrain = new LinkedList<>();
        System.out.println("Laplace Smoothing K = " + k);
        avaragehoursSTD = new deskriptif();
        timespendcompanySTD = new deskriptif();
        numberprojectSTD = new deskriptif();
        
        satisfactionSTD = new deskriptif();
        evaluationSTD = new deskriptif();
        
        Set<String> Division = new HashSet<>();
        Division.add(null);
        Set<String> Salary = new HashSet<>();
        //Data statistik data latih
        dataLatih.stream().forEachOrdered((d) -> {
            avaragehoursSTD.addValue(d.getAvaragehours());
            timespendcompanySTD.addValue(d.getTimespendcompany());
            numberprojectSTD.addValue(d.getNumberproject());
            Division.add(d.getDivision());
            Salary.add(d.getSalary()+"");
            satisfactionSTD.addValue(d.getSatisfaction());
            evaluationSTD.addValue(d.getEvaluation());
        });        
        
//        System.out.println("Division = " + Division);
//        System.out.println("Salary = " + Salary);
        
        naiveBayesPobabilitas workAccidentTrue = new naiveBayesPobabilitas("workAccident;True");
        naiveBayesPobabilitas workAccidentFalse = new naiveBayesPobabilitas("workAccident;False");
        
        naiveBayesPobabilitas promotionTrue = new naiveBayesPobabilitas("promotion;True");
        naiveBayesPobabilitas promotionFalse = new naiveBayesPobabilitas("promotion;False");
        
        naiveBayesPobabilitas avaragehoursTrue = new naiveBayesPobabilitas("avaragehours;True");
        naiveBayesPobabilitas avaragehoursFalse = new naiveBayesPobabilitas("avaragehours;False");

        naiveBayesPobabilitas timespendcompanyTrue = new naiveBayesPobabilitas("timespendcompany;True");
        naiveBayesPobabilitas timespendcompanyFalse = new naiveBayesPobabilitas("timespendcompany;False");
        
        naiveBayesPobabilitas numberprojectTrue = new naiveBayesPobabilitas("numberproject;True");
        naiveBayesPobabilitas numberprojectFalse = new naiveBayesPobabilitas("numberproject;False");

        naiveBayesPobabilitas evaluationTrue = new naiveBayesPobabilitas("evaluation;True");
        naiveBayesPobabilitas evaluationFalse = new naiveBayesPobabilitas("evaluation;False");

        naiveBayesPobabilitas satisfactionTrue = new naiveBayesPobabilitas("satisfaction;True");
        naiveBayesPobabilitas satisfactionFalse = new naiveBayesPobabilitas("satisfaction;False");


        
        List<naiveBayesPobabilitas> salaryList = new LinkedList<>();
        List<naiveBayesPobabilitas> divisionList = new LinkedList<>();
        for (String integer : Salary) {
            naiveBayesPobabilitas temp = new naiveBayesPobabilitas("Salary;"+integer);
            temp.setN(Salary.size()*1d);
            salaryList.add(temp);
        }
        
        for (String string : Division) {
            naiveBayesPobabilitas temp = new naiveBayesPobabilitas("Division;"+string);
            temp.setN(Division.size()*1d);
            divisionList.add(temp);
        }
        
        //
        tempTrain.add(workAccidentTrue);
        tempTrain.add(workAccidentFalse);
        tempTrain.add(promotionTrue);
        tempTrain.add(promotionFalse);
        tempTrain.add(avaragehoursTrue);
        tempTrain.add(avaragehoursFalse);
        tempTrain.add(timespendcompanyTrue);
        tempTrain.add(timespendcompanyFalse);
        tempTrain.add(numberprojectTrue);
        tempTrain.add(numberprojectFalse);
        tempTrain.add(satisfactionTrue);
        tempTrain.add(satisfactionFalse);
        tempTrain.add(evaluationTrue);
        tempTrain.add(evaluationFalse);        
        tempTrain.addAll(salaryList);
        tempTrain.addAll(divisionList);
        
                
        //hitung kejadian
        for (Dataset dataset : dataLatih) {
            counterNaiveBayes(workAccidentTrue,
                    workAccidentFalse,
                    dataset.getWorkaccident()==1?true:false,
                    dataset.getLefts());                            
            counterNaiveBayes(promotionTrue,
                    promotionFalse,
                    dataset.getPromotion()==1?true:false,
                    dataset.getLefts());                            
            
            counterNaiveBayes(avaragehoursTrue,
                    avaragehoursFalse,
                    dataset.getAvaragehours()>avaragehoursSTD.getMean()?true:false,
                    dataset.getLefts());                            
            counterNaiveBayes(timespendcompanyTrue,
                    timespendcompanyFalse,
                    dataset.getTimespendcompany()>timespendcompanySTD.getMean()?true:false,
                    dataset.getLefts());                            
            counterNaiveBayes(numberprojectTrue,
                    numberprojectFalse,
                    dataset.getNumberproject()>numberprojectSTD.getMean()?true:false,
                    dataset.getLefts());                            
            
            counterNaiveBayes(satisfactionTrue,
                    satisfactionFalse,
                    dataset.getSatisfaction()>satisfactionSTD.getMean()?true:false,
                    dataset.getLefts());                            
            
            counterNaiveBayes(evaluationTrue,
                    evaluationFalse,
                    dataset.getEvaluation()>evaluationSTD.getMean()?true:false,
                    dataset.getLefts());                            
            
            counterNiaveBayes(salaryList,"Salary;",dataset.getSalary().toString(), dataset.getLefts());
            counterNiaveBayes(divisionList,"Division;",dataset.getDivision(), dataset.getLefts());                          
            
            
            
        }
        
        //hitung probabilitas
        for (naiveBayesPobabilitas bp : tempTrain) {
            double pLeft= (bp.getCountLeft() + k) / (bp.getSumLeft() + (2d*k));
            double pNotLeft= (bp.getCountNotLeft()+ k) / (bp.getSumNotLeft()+ (2d*bp.getN()));
            
            bp.setProbabilitasLeft(pLeft);
            bp.setProbabilitasNotLeft(pNotLeft);
        }
        
        return tempTrain;
    }
    public static List<Dataset> KNN(List<Dataset> dataLatih,List<Dataset> dataUji,int k)
    {        
        System.out.println("dataLatih = " + dataLatih.size());
        System.out.println("dataUji = " + dataUji.size());
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        
        avaragehoursSTD = new deskriptif();
        timespendcompanySTD = new deskriptif();
        numberprojectSTD = new deskriptif();
        
        //Data statistik data latih
        dataLatih.stream().forEach((d) -> {
            avaragehoursSTD.addValue(d.getAvaragehours());
            timespendcompanySTD.addValue(d.getTimespendcompany());
            numberprojectSTD.addValue(d.getNumberproject());
        });
        
        
        dataUji
                .parallelStream()
                .forEach((d) -> {
            //Hitung jarak d ke semua himpunan datalatih
            dataLatih
                    .parallelStream()
                    .forEach((x) -> {
                    x.setDistance(euclideanFunction(d, x));                                    
            });
            //Temukan k tetanga terdekat terdeket
            //Sort
            dataLatih.sort((o2, o1) -> Double.compare(o2.getDistance(), o1.getDistance()));
            //Pilihan k terdekat
            List<Dataset> nearest = new LinkedList<>(dataLatih.subList(0, k));
            //vote
            double sumKelas = 0;
            sumKelas = nearest
                    .stream()
                    .map((dataset) ->
                            dataset.getLeftsDouble()).reduce(sumKelas,
                                    (accumulator, _item) -> accumulator + _item);
            double half = k/2d;
            if (sumKelas > half)
                d.setKelas(1d);
            else
                d.setKelas(0d);
        });
        return dataUji;
    }
    public static Attribute createAttribute(List<Dataset> Data,String key)
     {
        Function<Dataset, String> funcTemp = (Dataset a) -> (String) a.getMeta(key);         
        Map<String, Long> fun = Data
                .stream()
                .collect(Collectors.groupingBy(
                                     funcTemp , Collectors.counting()
                            ));
        
        Set<String> keySet = fun.keySet();
        FastVector fvNominalVal = new FastVector(keySet.size());
         for (String string : keySet) {
            fvNominalVal.addElement(string);             
         }
        return new Attribute(key, fvNominalVal);
     }
    public static  Instances createInstances(List<Dataset> Data)
     {
        int max = 10;
        FastVector listAttributes = new FastVector(max);
        listAttributes.addElement(new Attribute("satisfaction"));
        listAttributes.addElement(new Attribute("evaluation"));
        listAttributes.addElement(new Attribute("numberproject"));                
        listAttributes.addElement(new Attribute("avaragehours"));                
        listAttributes.addElement(new Attribute("timespendcompany"));                
        listAttributes.addElement(new Attribute("workAccident"));                
        listAttributes.addElement(new Attribute("promotion"));                
        listAttributes.addElement(new Attribute("salary"));                
        listAttributes.addElement(createAttribute(Data, "division"));
        listAttributes.addElement(createAttribute(Data, "leftsString"));

        
        
        Instances temp = new Instances("data", listAttributes, Data.size());
        temp.setClassIndex(max-1);        
         for (Dataset dataset : Data) {
            Instance x = new Instance(max);
            for (int i = 0; i < 10; i++) {
                 Attribute elementAt = (Attribute) listAttributes.elementAt(i);
                 Object meta = dataset.getMeta(elementAt.name()); 
                 if (meta instanceof Integer) {
                     int v = (int) meta;
                     double value = v*1d;                     
                     x.setValue(elementAt, value);
                 }
                 else if (meta instanceof Double) {                                 
                     x.setValue(elementAt, (double) meta);
                 }                 
                 else if (meta instanceof String) {                                 
                     x.setValue(elementAt, (String) meta);
                 }
                 
             }
//            System.out.println("x = " + x);     
            temp.add(x);
         }
         return temp;
     }
    public static C45 C45train(List<Dataset> dataLatih) throws Exception
    {
        C45 tree = new C45();
        Instances tranning = createInstances(dataLatih);
        tree.buildClassifier(tranning);
        return tree;
    }
    
    public static KNN KNNtrain(List<Dataset> dataLatih, int k) throws Exception
    {
        KNN knn = new KNN(k);
        Instances tranning = createInstances(dataLatih);
        knn.buildClassifier(tranning);
        return knn;
    }
    public static NB NBtrain(List<Dataset> dataLatih, int k) throws Exception
    {
        NB nb = new NB();
        Instances tranning = createInstances(dataLatih);
        nb.setKonstanta(k);
        nb.buildClassifier(tranning);
        return nb;
    }
    
    
    public static List<Dataset> ClassifierTesting(Classifier tree, List<Dataset> Data)
    {
        FastVector listAttributes = new FastVector(10);
        listAttributes.addElement(new Attribute("satisfaction"));
        listAttributes.addElement(new Attribute("evaluation"));
        listAttributes.addElement(new Attribute("numberproject"));                
        listAttributes.addElement(new Attribute("avaragehours"));                
        listAttributes.addElement(new Attribute("timespendcompany"));                
        listAttributes.addElement(new Attribute("workAccident"));                
        listAttributes.addElement(new Attribute("promotion"));                
        listAttributes.addElement(new Attribute("salary"));                
        listAttributes.addElement(createAttribute(Data, "division"));
        listAttributes.addElement(createAttribute(Data, "leftsString"));

        for (Dataset dataset : Data) {
            Instance singleInstance = singleInstance(listAttributes, dataset);
            double classifyInstance = 0;
            try {
                classifyInstance = tree.classifyInstance(singleInstance);
                dataset.setKelas(classifyInstance);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return Data;        
    }
     public static Instance singleInstance(FastVector listAttributes, Dataset data)
     {
         Instance single = new Instance(10);
         Instances dataUnlabeled = new Instances("TestInstances", listAttributes, 0);
         dataUnlabeled.add(single);
         dataUnlabeled.setClassIndex(dataUnlabeled.numAttributes() - 1);     
         single.setDataset(dataUnlabeled);
         for (int i = 0; i < 10; i++) {
                Attribute elementAt = (Attribute) listAttributes.elementAt(i);
                Object meta = data.getMeta(elementAt.name());              
                 if (meta instanceof Integer) {
                     int v = (int) meta;
                     double value = v*1d;                     
                     single.setValue(i, value);
                 }
                 else if (meta instanceof Double) {                                 
                     single.setValue(i, (double) meta);
                 }                 
                 else if (meta instanceof String) {                                 
                     single.setValue(i, (String) meta);
                 }
         }
//         System.out.println("single = " + single);
         return single;
     }    
    public static double euclideanFunction(Dataset y, Dataset x)
    {               
        //Kontinus var
        double satisfactionDelta = Math.pow(y.getSatisfaction() - x.getSatisfaction(), 2);
        double evaluationDelta = Math.pow(y.getEvaluation()- x.getEvaluation(), 2);
        //Boolean Variabel
        double workaccidentDelta = Math.pow(y.getWorkaccident()- x.getWorkaccident(), 2);
        double promotionDelta = Math.pow(y.getPromotion()- x.getPromotion(), 2);
        //Kategorical
        double DivisionDelta = y.getDivision().equals(x.getDivision())?1d:0d;
        double SalaryDelta = y.getSalary().equals(x.getSalary())?1d:0d;
        //Numeric
            Double NormalisasiAvarageHoursY = Normalisasi(y.getAvaragehours(), avaragehoursSTD);
            Double NormalisasiAvarageHoursX = Normalisasi(x.getAvaragehours(), avaragehoursSTD);
        double avaragehoursDelta = Math.pow(NormalisasiAvarageHoursY - NormalisasiAvarageHoursX, 2);
            Double NormalisasiNumberprojectY = Normalisasi(y.getNumberproject(), numberprojectSTD);
            Double NormalisasiNumberprojectX = Normalisasi(x.getNumberproject(), numberprojectSTD);
        double NumberprojectDelta = Math.pow(NormalisasiNumberprojectY - NormalisasiNumberprojectX, 2);
            Double NormalisasiTimespendcompanyY = Normalisasi(y.getTimespendcompany(), timespendcompanySTD);
            Double NormalisasiTimespendcompanyX = Normalisasi(x.getTimespendcompany(), timespendcompanySTD);
        double timespendcompanyDelta = Math.pow(NormalisasiTimespendcompanyY - NormalisasiTimespendcompanyX, 2);
                
        //Sum       
        double sum = 
                  satisfactionDelta 
                + evaluationDelta
                + workaccidentDelta
                + promotionDelta
                + DivisionDelta
                + SalaryDelta
                + avaragehoursDelta
                + NumberprojectDelta
                + timespendcompanyDelta
                ;
        return Math.sqrt(sum);
    }
    
    public static Double Normalisasi(Integer origin, deskriptif desk )
    {
        return (origin - desk.getMean()) / (desk.getMax() - desk.getMin());
    }

    private static void counterNiaveBayes(List<naiveBayesPobabilitas> list,String Key,String value, Integer DataClass)
    {
        for (naiveBayesPobabilitas p : list) {
            if (p.getAtribut().equals(Key+value)) {
                p.addCount();
                if (DataClass == 1) {
                    p.addCountLeft();
                } else {
                    p.addCountNotLeft();
                }
            }
        }
        
        for (naiveBayesPobabilitas p : list) {
            if (DataClass == 1) {
                p.addSumLeft();
            }
            else {
                p.addSumNotLeft();
            }            
        }
    }
    private static void counterNaiveBayes(
            naiveBayesPobabilitas NBPTrue, 
            naiveBayesPobabilitas NBPFalse, 
            Boolean value, 
            Integer DataClass) {
            if (value) {
                NBPTrue.addCount();
                if (DataClass == 1) {
                    NBPTrue.addCountLeft();
                }
                else {
                    NBPTrue.addCountNotLeft();
                }
            }
            else    {
                NBPFalse.addCount();
                if (DataClass == 1) {
                    NBPFalse.addCountLeft();
                }
                else {
                    NBPFalse.addCountNotLeft();
                }
            
            }
            if (DataClass == 1) {
                NBPTrue.addSumLeft();
                NBPFalse.addSumLeft();
            }
            else {
                NBPTrue.addSumNotLeft();
                NBPFalse.addSumNotLeft();
            }
            
    }
}
