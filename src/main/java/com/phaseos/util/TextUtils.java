package com.phaseos.util;

import org.bukkit.ChatColor;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Jon - 05/21/2019 | 14:00
 * __________________
 *
 *  [2016] J&M Plugin Development 
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of J&M Plugin Development and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to J&M Plugin Development
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from J&M Plugin Development.
 */
public class TextUtils {
    public static String fmt(String txt) {
        return ChatColor.translateAlternateColorCodes('&', txt);
    }
}
