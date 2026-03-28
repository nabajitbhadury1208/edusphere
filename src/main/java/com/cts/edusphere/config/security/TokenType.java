package com.cts.edusphere.config.security;

/**
 * Enumerates the two JWT token types used by the EduSphere authentication system.
 *
 * <ul>
 *   <li>{@link #ACCESS}  – short-lived token sent with every protected API request</li>
 *   <li>{@link #REFRESH} – long-lived token used solely to obtain a new access token</li>
 * </ul>
 */
public enum TokenType {
    /** Short-lived JWT used to authenticate individual API requests. */
    ACCESS,
    /** Long-lived JWT used only to issue a new access token when the current one expires. */
    REFRESH
}
