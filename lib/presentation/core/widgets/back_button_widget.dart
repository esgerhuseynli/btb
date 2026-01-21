import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:go_router/go_router.dart';
import '../../../core/theme/app_theme.dart';

class BackButtonWidget extends StatelessWidget {
  final VoidCallback? onPressed;

  const BackButtonWidget({
    super.key,
    this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(left: 8.w, top: 18.h),
      child: Align(
        alignment: Alignment.centerLeft,
        child: IconButton(
          icon: Image.asset(
            'assets/icons/arrow-left.png',
            width: 24.w,
            height: 24.h,
          ),
          onPressed: onPressed ?? () => context.pop(),
        ),
      ),
    );
  }
}

