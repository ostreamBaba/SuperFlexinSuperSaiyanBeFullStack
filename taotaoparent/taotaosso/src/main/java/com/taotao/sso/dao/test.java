package com.taotao.sso.dao;
import java.util.*;

/**
 * @ Create by ostreamBaba on 18-12-1
 * @ 描述
 */

public class test {

    public static void main(String[] args) {
        //System.out.println(Integer.bitCount(15^8));
        main1( "123" );
        int nums[] = {-1, -2, -3};
        System.out.println(maximumProduct1(nums));
    }

    public int maximumProduct(int[] nums) {
        Arrays.sort(nums);
        int ed = nums.length - 1;
        int max1 = nums[ed] * nums[ed-1] * nums[ed-2];
        int max2 = nums[0] * nums[1] * nums[ed];
        return Math.max(max1, max2);
    }

    public static int maximumProduct1(int[] nums) {
        int max1 = Integer.MIN_VALUE;
        int max2 = Integer.MIN_VALUE;
        int max3 = Integer.MIN_VALUE;
        int min1 = Integer.MAX_VALUE;
        int min2 = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if(nums[i] > max1){
                max3 = max2;
                max2 = max1;
                max1 = nums[i];
            }else if(nums[i] > max2){
                max3 = max2;
                max2 = nums[i];
            }else if(nums[i] > max3){
                max3 = nums[i];
            }

            if(nums[i] < min1){
                min2 = min1;
                min1 = nums[i];
            }else if(nums[i] < min2){
                min2 = nums[i];
            }

        }
        return Math.max(max1*max2*max3, min1*min2*max1);
    }

    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public ListNode deleteDuplicates(ListNode head) {
        ListNode cur = head;
        while (cur != null && cur.next != null){
            if(cur.val == cur.next.val){
                cur.next = cur.next.next;
            }else {
                cur = cur.next;
            }
        }
        return head;
    }

    public static void main1(String args) {
        System.out.println(2*0.75);
    }


}
