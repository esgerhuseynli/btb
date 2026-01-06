import 'package:flutter/material.dart';
import '../../../core/theme/app_theme.dart';

class AppAppBar extends StatelessWidget implements PreferredSizeWidget {
  final String? title;
  final List<Widget>? actions;
  final Widget? leading;
  final bool automaticallyImplyLeading;

  const AppAppBar({
    super.key,
    this.title,
    this.actions,
    this.leading,
    this.automaticallyImplyLeading = true,
  });

  @override
  Widget build(BuildContext context) {
    return AppBar(
      title: title != null
          ? Text(
              title!,
              style: const TextStyle(
                color: AppTheme.textColor,
                fontSize: 18,
                fontWeight: FontWeight.w600,
              ),
            )
          : null,
      leading: leading,
      automaticallyImplyLeading: automaticallyImplyLeading,
      actions: actions,
      backgroundColor: AppTheme.white,
      elevation: 0,
      iconTheme: const IconThemeData(color: AppTheme.textColor),
    );
  }

  @override
  Size get preferredSize => const Size.fromHeight(kToolbarHeight);
}



