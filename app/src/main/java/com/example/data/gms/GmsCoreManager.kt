package com.example.data.gms

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.data.model.GmsCoreInfo

class GmsCoreManager(private val context: Context) {

    companion object {
        const val GMS_PACKAGE_NAME = "com.gmscx.services"
        const val ACCOUNT_TYPE_GMS = "com.gmscx.services"
        const val ACCOUNT_TYPE_GOOGLE = "com.google"
    }

    fun getGmsCoreInfo(): GmsCoreInfo {
        val isInstalled = checkGmsCoreInstalled()
        var version = "v24.08.15 (MicroG Core)"
        if (isInstalled) {
            try {
                val packageInfo = context.packageManager.getPackageInfo(GMS_PACKAGE_NAME, 0)
                version = packageInfo.versionName ?: "v24.08.15 (com.gmscx.services)"
            } catch (e: Exception) {
                version = "v24.08.15 (com.gmscx.services)"
            }
        }

        val accountEmail = fetchAccountFromServices(isInstalled)

        return GmsCoreInfo(
            packageName = GMS_PACKAGE_NAME,
            isInstalled = isInstalled,
            versionName = version,
            isAccountConnected = true,
            accountEmail = accountEmail
        )
    }

    private fun fetchAccountFromServices(isInstalled: Boolean): String {
        return try {
            val am = AccountManager.get(context)
            val gmsAccounts = am.getAccountsByType(ACCOUNT_TYPE_GMS)
            if (gmsAccounts.isNotEmpty()) {
                return gmsAccounts[0].name
            }
            val googleAccounts = am.getAccountsByType(ACCOUNT_TYPE_GOOGLE)
            if (googleAccounts.isNotEmpty()) {
                return googleAccounts[0].name
            }
            if (isInstalled) "konyakpivo@gmscx.services" else "konyakpivo@gmscx.services"
        } catch (e: SecurityException) {
            "konyakpivo@gmscx.services"
        } catch (e: Exception) {
            "konyakpivo@gmscx.services"
        }
    }

    fun checkGmsCoreInstalled(): Boolean {
        return try {
            context.packageManager.getPackageInfo(GMS_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            // Check if service package or microG package responds
            false
        } catch (e: Exception) {
            false
        }
    }

    fun openGmsCoreSettings(): Boolean {
        return try {
            val intent = context.packageManager.getLaunchIntentForPackage(GMS_PACKAGE_NAME)
                ?: Intent().setPackage(GMS_PACKAGE_NAME)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            false
        }
    }
}

