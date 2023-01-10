/*
 * Name: <Rodney Bradford Deransburg>
 * EID: <rmd2784>
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Your solution goes in this class.
 *
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 *
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution. However, do not add extra import statements to this file.
 */
public class Program1 extends AbstractProgram1 {

    /**
     * Determines whether a candidate Matching represents a solution to the stable matching problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    @Override
    public boolean isStableMatching(Matching problem) {
        /* TODO implement this function */
        ArrayList<ArrayList<Integer>> u_pref_list = problem.getUniversityPreference();
        ArrayList<ArrayList<Integer>> s_pref_list = problem.getStudentPreference();
        ArrayList<ArrayList<Integer>> u_inv_pref = new ArrayList<>();
        ArrayList<ArrayList<Integer>> s_inv_pref = new ArrayList<>();
        ArrayList<Integer> u_pref;
        ArrayList<Integer> s_pref;
        ArrayList<ArrayList<Integer>> u_preferred_over;
        ArrayList<Integer> Matches = problem.getStudentMatching();

        ArrayList<Integer> temp_inv_list = new ArrayList<>(u_pref_list.get(0).size());
        //copy u_pref into temp_inv_list since java is fucking pass by reference instead of pass by value fuck you java
        for(int i = 0; i < u_pref_list.get(0).size();i++){
            temp_inv_list.add(i, u_pref_list.get(0).get(i));
        }
        for(int i = 0; i < u_pref_list.size();i++){
            for(int j = 0; j < u_pref_list.get(i).size(); j++){
                int inv_index = u_pref_list.get(i).get(j);
                temp_inv_list.set(inv_index, j);
            }
            ArrayList<Integer> fill = new ArrayList<>();
            for(int z = 0; z < temp_inv_list.size(); z++){
                fill.add(z, temp_inv_list.get(z));
            }
            u_inv_pref.add(fill);
        }
        //create inverse_pref list for students
        //clear temp_inv
        temp_inv_list.clear();
        //clone stu pref into it
        for(int i = 0; i < s_pref_list.get(0).size();i++){
            temp_inv_list.add(i, s_pref_list.get(0).get(i));
        }
        for(int i = 0; i < s_pref_list.size();i++){
            for(int j = 0; j < s_pref_list.get(i).size(); j++){
                int inv_index = s_pref_list.get(i).get(j);
                temp_inv_list.set(inv_index, j);
            }
            ArrayList<Integer> fill = new ArrayList<>();
            for(int z = 0; z < temp_inv_list.size(); z++){
                fill.add(z, temp_inv_list.get(z));
            }
            s_inv_pref.add(fill);
        }
        //instability 1
        for(int i = 0; i < Matches.size(); i++){
            int uni = Matches.get(i);
            if(uni != -1){
                int index_cur_s_pref = u_pref_list.get(uni).indexOf(i);
                u_pref = u_pref_list.get(uni);
                for(int j = 0; j < index_cur_s_pref; j++){
                    int s = u_pref.get(j);
                    if(Matches.get(s) == -1)
                        return false;
                }
            }
        }
        //instability 2
        for(int i = 0; i < Matches.size(); i++){
            int uni = Matches.get(i);
            if(uni != -1){
                int index_cur_s_pref = u_pref_list.get(uni).indexOf(i);
                u_pref = u_pref_list.get(uni);
                for(int j = 0; j < index_cur_s_pref; j++){
                    int s = u_pref.get(j);
                    if(Matches.get(s) == -1){
                        continue;
                    }
                    int s_uni = Matches.get(s);
                    int cur_s_pref = s_inv_pref.get(s).get(s_uni);
                    int other_s_pref = s_inv_pref.get(s).get(uni);
                    if(s_inv_pref.get(s).get(s_uni) > s_inv_pref.get(s).get(uni)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMatchingGaleShapley_universityoptimal(Matching problem) {
        /* TODO implement this function */
        ArrayList<ArrayList<Integer>> u_pref = problem.getUniversityPreference();
        ArrayList<ArrayList<Integer>> s_pref = problem.getStudentPreference();
        ArrayList<Integer> remaining_positions = problem.getUniversityPositions(); //index is uni, value is #pstn
        ArrayList<ArrayList<Integer>> u_inv_pref = new ArrayList<>();
        ArrayList<ArrayList<Integer>> s_inv_pref = new ArrayList<>();
        QueueItems current_uni= new QueueItems();
        ArrayList<Integer> matched_uni = new ArrayList<>();
        ArrayList<Integer> Matches = new ArrayList<>();
        Queue<QueueItems> offer_queue = new LinkedList<>();

        //initialize u & s to be free
        for(int i = 0; i < s_pref.size();i++){
            Matches.add(i,-1);
        }
        //add universities in queue to determine order of propositions
        for(int i = 0; i < u_pref.size(); i++){
            for(int j = 0; j < remaining_positions.get(i);j++){
                QueueItems uni = new QueueItems(i, u_pref.get(i));
                offer_queue.add(uni);
            }
        }
        //create inverse_pref list for uni
        ArrayList<Integer> temp_inv_list = new ArrayList<>(u_pref.get(0).size());
        //copy u_pref into temp_inv_list since java is fucking pass by reference instead of pass by value fuck you java
        for(int i = 0; i < u_pref.get(0).size();i++){
            temp_inv_list.add(i, u_pref.get(0).get(i));
        }
        for(int i = 0; i < u_pref.size();i++){
            for(int j = 0; j < u_pref.get(i).size(); j++){
                int inv_index = u_pref.get(i).get(j);
                temp_inv_list.set(inv_index, j);
            }
            ArrayList<Integer> fill = new ArrayList<>();
            for(int z = 0; z < temp_inv_list.size(); z++){
                fill.add(z, temp_inv_list.get(z));
            }
            u_inv_pref.add(fill);
        }
        //create inverse_pref list for students
        //clear temp_inv
        temp_inv_list.clear();
        //clone stu pref into it
        for(int i = 0; i < s_pref.get(0).size();i++){
            temp_inv_list.add(i, s_pref.get(0).get(i));
        }
        for(int i = 0; i < s_pref.size();i++){
            for(int j = 0; j < s_pref.get(i).size(); j++){
                int inv_index = s_pref.get(i).get(j);
                temp_inv_list.set(inv_index, j);
            }
            ArrayList<Integer> fill = new ArrayList<>();
            for(int z = 0; z < temp_inv_list.size(); z++){
                fill.add(z, temp_inv_list.get(z));
            }
            s_inv_pref.add(fill);
        }
        //while free man m has woman to propose to
        while(!offer_queue.isEmpty()){

            //pull university
            current_uni = offer_queue.remove();
            //traverse through uni preferences and send offers
            for(int i = 0; i < current_uni.uni_pref.size(); i++){
                int cur_u_pref = current_uni.uni_pref.get(i);
                if(Matches.get(cur_u_pref) == -1){
                    Matches.set(cur_u_pref, current_uni.uni_position);
                    break;
                }
                else{
                    int matched_student_uni = Matches.get(cur_u_pref);
                    int proposing_student_uni = current_uni.uni_position;
                    int matched_stu_pref =  s_inv_pref.get(cur_u_pref).get(matched_student_uni);
                    int proposing_stu_pref = s_inv_pref.get(cur_u_pref).get(proposing_student_uni);
                    if(proposing_stu_pref < matched_stu_pref){
                        //swap
                        QueueItems cur_match = new QueueItems(matched_student_uni, u_pref.get(matched_student_uni));
                        offer_queue.add(cur_match);
                        //
                        Matches.set(cur_u_pref, current_uni.uni_position);
                        int deargodsavemeplease = 444;
                        break;
                    }
                    int testestestset = 123;
                }
            }
        }
        problem.setStudentMatching(Matches);
        return problem;
    }


    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMatchingGaleShapley_studentoptimal(Matching problem) {
        /* TODO implement this function */
        ArrayList<ArrayList<Integer>> u_pref = problem.getUniversityPreference();
        ArrayList<ArrayList<Integer>> s_pref = problem.getStudentPreference();
        ArrayList<Integer> remaining_positions = problem.getUniversityPositions(); //index is uni, value is #pstn
        ArrayList<ArrayList<Integer>> u_inv_pref = new ArrayList<>();
        ArrayList<ArrayList<Integer>> s_inv_pref = new ArrayList<>();
        s_QueueItems current_s= new s_QueueItems();
        ArrayList<Integer> matched_uni = new ArrayList<>();
        ArrayList<Integer> Matches = new ArrayList<>();
        Queue<s_QueueItems> offer_queue = new LinkedList<>();

        //initialize u & s to be free
        for(int i = 0; i < s_pref.size();i++){
            Matches.add(i,-1);
        }
        //add students in queue to determine order of propositions
        for(int i = 0; i < s_pref.size(); i++){
                s_QueueItems s = new s_QueueItems(i, s_pref.get(i));
                offer_queue.add(s);
        }
        //create inverse_pref list for uni
        ArrayList<Integer> temp_inv_list = new ArrayList<>(u_pref.get(0).size());
        //copy u_pref into temp_inv_list since java is fucking pass by reference instead of pass by value fuck you java
        for(int i = 0; i < u_pref.get(0).size();i++){
            temp_inv_list.add(i, u_pref.get(0).get(i));
        }
        for(int i = 0; i < u_pref.size();i++){
            for(int j = 0; j < u_pref.get(i).size(); j++){
                int inv_index = u_pref.get(i).get(j);
                temp_inv_list.set(inv_index, j);
            }
            ArrayList<Integer> fill = new ArrayList<>();
            for(int z = 0; z < temp_inv_list.size(); z++){
                fill.add(z, temp_inv_list.get(z));
            }
            u_inv_pref.add(fill);
        }
        //create inverse_pref list for students
        //clear temp_inv
        temp_inv_list.clear();
        //clone stu pref into it
        for(int i = 0; i < s_pref.get(0).size();i++){
            temp_inv_list.add(i, s_pref.get(0).get(i));
        }
        for(int i = 0; i < s_pref.size();i++){
            for(int j = 0; j < s_pref.get(i).size(); j++){
                int inv_index = s_pref.get(i).get(j);
                temp_inv_list.set(inv_index, j);
            }
            ArrayList<Integer> fill = new ArrayList<>();
            for(int z = 0; z < temp_inv_list.size(); z++){
                fill.add(z, temp_inv_list.get(z));
            }
            s_inv_pref.add(fill);
        }
        while(!offer_queue.isEmpty()){
            current_s = offer_queue.remove();
            //traverse through student preferences and send offers
            for(int i = 0; i < current_s.s_pref.size(); i++){
                int cur_s_pref = current_s.s_pref.get(i);
                if(Matches.indexOf(cur_s_pref) == -1){ //preferred uni hasnt been matched yet
                    Matches.set(current_s.s_position, cur_s_pref);
                    int uni_pos = remaining_positions.get(cur_s_pref);
                    uni_pos--;
                    remaining_positions.set(cur_s_pref, uni_pos);
                    //decrement uni that just got matched
                    break;
                }
                else{
                    int matched_student_uni = Matches.indexOf(cur_s_pref); //index of student uni is matched to
                    int proposing_student_uni = current_s.s_position;
                    int matched_uni_pref = u_inv_pref.get(cur_s_pref).get(matched_student_uni);
                    int proposing_uni_pref = u_inv_pref.get(cur_s_pref).get(proposing_student_uni);
                    if(proposing_uni_pref < matched_uni_pref && remaining_positions.get(cur_s_pref) > 0){
                        s_QueueItems cur_match = new s_QueueItems(matched_student_uni, s_pref.get(matched_student_uni));
                        offer_queue.add(cur_match);
                        Matches.set(current_s.s_position, cur_s_pref);
                        int positionsleft = remaining_positions.get(cur_s_pref) - 1;
                        remaining_positions.set(cur_s_pref, positionsleft);
                        break;
                    }
                    /*
                    int matched_student_uni = Matches.get(cur_u_pref);
                    int proposing_student_uni = current_uni.uni_position;
                    int matched_stu_pref =  s_inv_pref.get(cur_u_pref).get(matched_student_uni);
                    int proposing_stu_pref = s_inv_pref.get(cur_u_pref).get(proposing_student_uni);
                    if(proposing_stu_pref < matched_stu_pref){
                        //swap
                        QueueItems cur_match = new QueueItems(matched_student_uni, u_pref.get(matched_student_uni));
                        offer_queue.add(cur_match);
                        //
                        Matches.set(cur_u_pref, current_uni.uni_position);
                        int deargodsavemeplease = 444;
                        break;*/
                    }
                }
            }
        problem.setStudentMatching(Matches);
        return problem;
        }

    }
