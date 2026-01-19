import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../core/theme/app_theme.dart';

class PageIndicator extends StatelessWidget {
  final int currentPage;
  final int pageCount;

  const PageIndicator({
    super.key,
    required this.currentPage,
    required this.pageCount,
  });

  @override
  Widget build(BuildContext context) {
    if (pageCount <= 1) {
      return const SizedBox.shrink();
    }

    return Container(
      padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 20.h),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: List.generate(
          pageCount,
          (index) => Container(
            width: 22.19.w,
            height: 3.362.h,
            margin: EdgeInsets.only(
              right: index < pageCount - 1 ? 5.w : 0,
            ),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(5.r),
              color: index == currentPage
                  ? Colors.black
                  : const Color(0xFF393939).withOpacity(0.05),
            ),
          ),
        ),
      ),
    );
  }
}

